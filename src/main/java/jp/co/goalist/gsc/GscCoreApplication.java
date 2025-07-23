package jp.co.goalist.gsc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.goalist.gsc.services.AccountService;

@RestController
@EnableAsync
@EnableJpaAuditing
@EnableFeignClients
@EnableJpaRepositories
@SpringBootApplication
public class GscCoreApplication {

    @Autowired
    private AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(GscCoreApplication.class, args);
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("gsc-be-core is running");
    }

    @GetMapping("/public/new-operator-account")
    public ResponseEntity<String> createNewOperatorAccount() {
        accountService.createOperatorAccount();
        return ResponseEntity.ok("New operator admin account created successfully");
    }
}
