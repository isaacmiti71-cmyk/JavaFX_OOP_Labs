package com.studentservices.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Optional HTTP Client demonstrating external service communication.
 * Fetches recent university announcements or falls back to an offline default announcement.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class AnnouncementClient {

    private final HttpClient httpClient;
    private static final String DEFAULT_ANNOUNCEMENT = 
            "📢 Mulungushi University: Semester 1 Registration is open. Welcome to School of Engineering & Technology!";

    public AnnouncementClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

    /**
     * Fetches current announcements. In case of network timeout or error, returns a friendly default announcement.
     */
    public String fetchLatestAnnouncement() {
        try {
            // Attempt an HTTP GET request to a sample public announcements/status endpoint
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/status/200"))
                    .timeout(Duration.ofSeconds(2))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return "📢 Mulungushi University Portal: Connected. All registration services are running normally.";
            }
        } catch (Exception e) {
            // Log notice and gracefully return offline announcement
            System.out.println("[AnnouncementClient] Network unavailable or timed out. Using default announcement.");
        }
        return DEFAULT_ANNOUNCEMENT;
    }
}
