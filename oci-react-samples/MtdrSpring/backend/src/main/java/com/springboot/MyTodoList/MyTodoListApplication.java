package com.springboot.MyTodoList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.springboot.MyTodoList.controller.ToDoItemBotController;
import com.springboot.MyTodoList.controller.ProjectController;
import com.springboot.MyTodoList.controller.TaskStatusController;
import com.springboot.MyTodoList.controller.TelegramUserController;
import com.springboot.MyTodoList.controller.UserTypeController;
import com.springboot.MyTodoList.util.BotMessages;

import com.springboot.MyTodoList.service.BotMenuService;
import com.springboot.MyTodoList.service.BotOptionService;
import com.springboot.MyTodoList.service.ConversationService;
import com.springboot.MyTodoList.service.MessageService;
import com.springboot.MyTodoList.service.SprintService;
import com.springboot.MyTodoList.service.SprintUpdateService;
import com.springboot.MyTodoList.service.TaskService;
import com.springboot.MyTodoList.service.TaskUpdateService;
import com.springboot.MyTodoList.service.TeamService;
import com.springboot.MyTodoList.service.TeamTypeService;
import com.springboot.MyTodoList.service.UpdateTypeService;

@SpringBootApplication
public class MyTodoListApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(MyTodoListApplication.class);

	@Autowired
	private BotMenuService botMenuService;

	@Autowired
	private BotOptionService botOptionService;

	@Autowired
	private ConversationService conversationService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private ProjectController projectController;

	@Autowired
	private SprintService sprintService;

	@Autowired
	private SprintUpdateService sprintUpdateService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskStatusController taskStatusController;

	@Autowired
	private TaskUpdateService taskUpdateService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private TeamTypeService teamTypeService;

	@Autowired
	private TelegramUserController telegramUserController;

	@Autowired
	private UpdateTypeService updateTypeService;

	@Autowired
	private UserTypeController userTypeController;

	@Value("${telegram.bot.token}")
	private String telegramBotToken;

	@Value("${telegram.bot.name}")
	private String botName;

	public static void main(String[] args) {
		SpringApplication.run(MyTodoListApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(new ToDoItemBotController(telegramBotToken, botName, telegramUserController,
			taskService, userTypeController, taskStatusController, projectController));
			logger.info(BotMessages.BOT_REGISTERED_STARTED.getMessage());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
