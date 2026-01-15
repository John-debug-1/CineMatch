package org.MY_APP.main.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.MY_APP.main.dto.AiQuizQuestionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import java.util.Collections;


@Service
public class QuizAiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;

    private final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=";

    @Value("${huggingface.api.key}")
    private String HF_API_KEY;

    @Value("${huggingface.actor.model-url}")
    private String HF_ACTOR_URL;

    private static final Map<String, String> ACTOR_MAP = Map.of(
            "angry", "Samuel L. Jackson",
            "disgust", "Johnny Depp",
            "fear", "Jack Nicholson",
            "happy", "Tom Hanks",
            "sad", "Harrison Ford",
            "surprise", "Jim Carrey",
            "neutral", "Ryan Gosling"
    );


    public List<AiQuizQuestionDto> generateQuizFromAI() {

        String prompt = """
        Create a movie quiz with 5 multiple-choice questions.
        Each question must have 4 options and 1 correct answer.
        Return ONLY valid JSON in this format:

        {
          "questions": [
            {
              "question": "Who played Iron Man?",
              "options": ["Chris Evans", "Robert Downey Jr.", "Tom Hardy", "Brad Pitt"],
              "correct": "Robert Downey Jr."
            }
          ]
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body;
        try {
            body = objectMapper.writeValueAsString(
                    Map.of(
                            "contents", List.of(
                                    Map.of(
                                            "parts", List.of(
                                                    Map.of("text", prompt)
                                            )
                                    )
                            )
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to build Gemini request body", e);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        System.out.println("✅ GEMINI REQUEST BODY:");
        System.out.println(body);

        ResponseEntity<String> response = restTemplate.exchange(
                GEMINI_URL + GEMINI_API_KEY,
                HttpMethod.POST,
                entity,
                String.class
        );

        System.out.println("✅ GEMINI RAW RESPONSE:");
        System.out.println(response.getBody());

        return parseQuestionsFromResponse(response.getBody());
    }

    public List<AiQuizQuestionDto> generateActorQuizFromAI() {

        String prompt = """
    Create a movie quiz with 5 multiple-choice questions.
    The goal is to guess the REAL NAME of an actor from the movies they starred in.

    Each question must follow this format:
    "This actor starred in: Movie1, Movie2, Movie3. Who is this actor?"

    Each question must have 4 options and only 1 correct answer.

    Return ONLY valid JSON in this format:

    {
      "questions": [
        {
          "question": "This actor starred in: Titanic, Inception, The Revenant. Who is this actor?",
          "options": ["Brad Pitt", "Leonardo DiCaprio", "Tom Cruise", "Matt Damon"],
          "correct": "Leonardo DiCaprio"
        }
      ]
    }
    """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body;
        try {
            body = objectMapper.writeValueAsString(
                    Map.of(
                            "contents", List.of(
                                    Map.of(
                                            "parts", List.of(
                                                    Map.of("text", prompt)
                                            )
                                    )
                            )
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to build Gemini request body", e);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GEMINI_URL + GEMINI_API_KEY,
                HttpMethod.POST,
                entity,
                String.class
        );

        return parseQuestionsFromResponse(response.getBody());
    }


    public String findMatchingActor(byte[] imageBytes) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + HF_API_KEY);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("X-Wait-For-Model", "true");

            HttpEntity<byte[]> entity = new HttpEntity<>(imageBytes, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    HF_ACTOR_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // ✅ Debug
            System.out.println("FULL HF RESPONSE: " + response.getBody());

            JsonNode root = objectMapper.readTree(response.getBody());

            if (root.isArray() && root.size() > 0) {
                String emotion = root.get(0).path("label").asText().toLowerCase();
                System.out.println("DETECTED EMOTION: " + emotion);

                return ACTOR_MAP.getOrDefault(emotion, "Robert Downey Jr.");
            }

            return "Brad Pitt (Response Format Error)";

        } catch (Exception e) {
            System.err.println("CRITICAL API ERROR: " + e.getMessage());
            return "Brad Pitt (Error: " + e.getMessage() + ")";
        }
    }



    private List<AiQuizQuestionDto> parseQuestionsFromResponse(String json) {

        try {
            JsonNode root = objectMapper.readTree(json);

            String text = root
                    .get("candidates")
                    .get(0)
                    .get("content")
                    .get("parts")
                    .get(0)
                    .get("text")
                    .asText()
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            System.out.println("✅ RAW AI TEXT:");
            System.out.println(text);

            JsonNode quizNode = objectMapper.readTree(text);
            JsonNode questionsNode = quizNode.get("questions");

            List<AiQuizQuestionDto> questions = new ArrayList<>();

            for (JsonNode q : questionsNode) {

                AiQuizQuestionDto dto = new AiQuizQuestionDto();

                dto.setQuestion(q.get("question").asText());
                dto.setOptionA(q.get("options").get(0).asText());
                dto.setOptionB(q.get("options").get(1).asText());
                dto.setOptionC(q.get("options").get(2).asText());
                dto.setOptionD(q.get("options").get(3).asText());
                dto.setCorrect(q.get("correct").asText());

                questions.add(dto);
            }

            return questions;

        } catch (Exception e) {
            throw new RuntimeException("AI Quiz Parsing Failed", e);
        }
    }

}
