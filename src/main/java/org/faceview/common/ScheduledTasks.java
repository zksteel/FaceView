package org.faceview.common;

import org.faceview.user.entity.User;
import org.faceview.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class ScheduledTasks {

    private final UserService userService;

    @Autowired
    public ScheduledTasks(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 30 14 * * *")
    public void testSchedule() {
        System.out.println("executing");
        List<User> allUsers = this.userService.findAll();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\ivan\\Desktop\\users.txt")));
            for (User user : allUsers) {
                writer.append(user.getId())
                        .append(" --> username: ")
                        .append(user.getUsername())
                        .append("  email: ")
                        .append(user.getEmail())
                        .append(System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
