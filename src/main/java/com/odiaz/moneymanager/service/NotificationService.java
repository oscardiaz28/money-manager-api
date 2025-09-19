package com.odiaz.moneymanager.service;

import com.odiaz.moneymanager.dto.expense.ExpenseDTO;
import com.odiaz.moneymanager.model.ExpenseEntity;
import com.odiaz.moneymanager.model.ProfileEntity;
import com.odiaz.moneymanager.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    private final ExpenseService expenseService;
    private final EmailService emailService;
    private final ProfileRepository profileRepository;
    private final SpringTemplateEngine templateEngine;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    public NotificationService(ExpenseService expenseService, EmailService emailService, ProfileRepository profileRepository, SpringTemplateEngine templateEngine) {
        this.expenseService = expenseService;
        this.emailService = emailService;
        this.profileRepository = profileRepository;
        this.templateEngine = templateEngine;
    }

    @Scheduled(cron = "0 0 22 * * *", zone = "America/Lima") // every day at 10pm, send this reminder email
    // @Scheduled(cron = "0 * * * * *", zone = "America/Lima") every minute
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for( ProfileEntity profile : profiles ){
            String body = "Hi " + profile.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in Money Manager.<br><br>"
                    + "<a href=" + this.frontendUrl + ">Go to Money Manager </a>"
                    + "<br><br>Best regards, <br>Money Manager Team";
            emailService.sendEmail(profile.getEmail(), "Daily Reminder: Add your incomes and expenses", body);
        }
        log.info("Job completed: sendDailyIncomeExpenseReminder()");
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "America/Lima")
    // @Scheduled(cron = "0 * * * * *", zone = "America/Lima")
    public void sendDailyExpenseSummary(){
        log.info("Job started: sendDailyExpenseSummary()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for( ProfileEntity profile : profiles ){
            LocalDate today = LocalDate.now();
            List<ExpenseDTO> expenses = expenseService.getExpensesForUserOnDate(profile.getId(), today);
            System.out.println(expenses);
            if(!expenses.isEmpty()){
                Context context = new Context();
                context.setVariable("profileName", profile.getFullName());
                context.setVariable("expenses", expenses);
                String body = templateEngine.process("daily-summary", context);
                emailService.sendEmail(profile.getEmail(), "Your Daily Expense Summary", body);
            }
        }
        log.info("Job completed: sendDailyExpenseSummary()");
    }

}
