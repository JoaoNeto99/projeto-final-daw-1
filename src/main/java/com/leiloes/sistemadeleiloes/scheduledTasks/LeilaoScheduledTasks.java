package com.leiloes.sistemadeleiloes.scheduledTasks;

import com.leiloes.sistemadeleiloes.service.LeilaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;


@Component
public class LeilaoScheduledTasks {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM/dd/yyyy HH:mm:ss");

    @Autowired
    LeilaoService leilaoService;

    @Scheduled(cron = "0 0 * * * *")
    public void performTaskUsingCron() {
        leilaoService.expiraComData();
        //leilaoService.finalizaExpirado();
//        System.out.println("EXPIRADO");
//        System.out.println("Regular task performed using Cron at "
//                + dateFormat.format(new Date()));

    }
}
