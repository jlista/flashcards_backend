package com.flashcards;

import com.flashcards.model.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

  @GetMapping("/hello")
  public Message sayHello(@RequestParam(defaultValue = "World") String name) {
    return new Message("Hello, " + name + "!");
  }

  @PostMapping("/echo")
  public Message echoMessage(@RequestBody Message message) {
    return new Message("You said: " + message.getContent());
  }
}