package app.manager.controller;

import app.manager.ExecuteRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ManagerController {

    private final ExecuteRequest executeRequest;

    public ManagerController(ExecuteRequest executeRequest) {
        this.executeRequest = executeRequest;
    }

    @PostMapping("/api/chat/request")
    public Map<String, String> getRequestToChatGpt(@RequestBody Map<String, String> params) {
        System.out.println(params);
        return executeRequest.execute(params);
    }
}