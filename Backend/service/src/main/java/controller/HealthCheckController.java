package controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController extends BaseController{
    @GetMapping("/healthCheck")
    public ResponseEntity<String> handle() {
        /* stub controller for preventing Heroku dyno idling */
        logger.info("[HEALTH CHECK] Uptime Robot Pinged!");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
