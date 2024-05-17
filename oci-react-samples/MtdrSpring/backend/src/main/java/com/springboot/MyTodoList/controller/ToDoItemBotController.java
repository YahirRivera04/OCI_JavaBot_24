package com.springboot.MyTodoList.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.service.TaskService;
import com.springboot.MyTodoList.service.TelegramUserService;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotMessages;

public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private ToDoItemService toDoItemService;
	private TelegramUserService telegramUserService;
	private TaskService taskService;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, ToDoItemService toDoItemService, TelegramUserService telegramUserService, TaskService taskService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.toDoItemService = toDoItemService;
		this.telegramUserService = telegramUserService;
		this.taskService = taskService;
		this.botName = botName;
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();

			TelegramUser telegramUser = new TelegramUser();
			Long UserId = null;

			
			// If the bot detects the start command
			if(messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())){

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.LOG_IN_MESSAGE.getMessage());
				
				try{
					execute(messageToTelegram);
				}
				catch(TelegramApiException e){
					logger.error(e.getLocalizedMessage(), e);
				}

			}
			// If the bot detects the command /response:"TelegramUserName"
			// /response:Yahir_Rivera04
			else if(messageTextFromTelegram.substring(0, 10).equals(BotCommands.RESPONSE_COMMAND.getCommand())){
				
				// Extracts the User name from the message
				String responseFromUser = messageTextFromTelegram.substring(10,messageTextFromTelegram.length());
				
				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText("Verifying the user: " + responseFromUser);					
				
				// Verify Telegram User Name from database and get Telegram User Id
				ResponseEntity<Integer> id = getTelegramUserId(responseFromUser);

				telegramUser.setChatId(chatId);
				telegramUser.setID(id.getBody());
				
				// Static transform from Integer to Long
				UserId = telegramUser.getID().longValue();

				try{
					execute(messageToTelegram);
					if(telegramUser.getID() != null){
						sendMessage(BotMessages.LOG_IN_SUCCESS.getMessage(), telegramUser.getChatId());
						
						// Update Telegram User ChatId
						ResponseEntity<String> response = updateTelegramUser(UserId, telegramUser);
						sendMessage(response.getBody(), telegramUser.getChatId());
					}
					else{
						sendMessage(BotMessages.LOG_IN_FAIL.getMessage(), telegramUser.getChatId());
					}
				}
				catch(TelegramApiException e){
					logger.error(e.getLocalizedMessage(), e);
				}				
			}
	}
}
	
	@Override
	public String getBotUsername() {		
		return botName;
	}

	// Verify Telegram User Name from database
	public ResponseEntity<Boolean> getUserByTelegramName(String TelegramName){
		return ResponseEntity.ok(telegramUserService.existsByTelegramName(TelegramName));
	}

	// Get Telegram User Id from database
	public ResponseEntity<Integer> getTelegramUserId(String TelegramName){
		return ResponseEntity.ok(telegramUserService.findTelegramUserId(TelegramName));
	}

	// Get Chat Id from database
    public ResponseEntity<Long> findChatId(@PathVariable Long id){
        return ResponseEntity.ok(telegramUserService.fndChatIdByTelegramUserId(id));
    }

	// Put Telegram User ChatId
    public ResponseEntity<String> updateTelegramUser(Long id, TelegramUser telegramUser){
		return ResponseEntity.ok(telegramUserService.updateChatId(id, telegramUser));
    }

	// Auxiliar Method to print messages
	public void sendMessage(String message, Long chatID){
		SendMessage messageToTelegram = new SendMessage();
		messageToTelegram.setChatId(chatID);
		messageToTelegram.setText(message);
		try{
			execute(messageToTelegram);
		}
		catch(TelegramApiException e){
			logger.error(e.getLocalizedMessage(), e);
		}
	}


	// // GET /todolist
	// public List<ToDoItem> getAllToDoItems() { 
	// 	return toDoItemService.findAll();
	// }
	// // GET BY ID /todolist/{id}
	// public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id) {
	// 	try {
	// 		ResponseEntity<ToDoItem> responseEntity = toDoItemService.getItemById(id);
	// 		return new ResponseEntity<ToDoItem>(responseEntity.getBody(), HttpStatus.OK);
	// 	} catch (Exception e) {
	// 		logger.error(e.getLocalizedMessage(), e);
	// 		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// 	}
	// }

	// // PUT /todolist
	// public ResponseEntity addToDoItem(@RequestBody ToDoItem todoItem) throws Exception {
	// 	ToDoItem td = toDoItemService.addToDoItem(todoItem);
	// 	HttpHeaders responseHeaders = new HttpHeaders();
	// 	responseHeaders.set("location", "" + td.getID());
	// 	responseHeaders.set("Access-Control-Expose-Headers", "location");
	// 	// URI location = URI.create(""+td.getID())

	// 	return ResponseEntity.ok().headers(responseHeaders).build();
	// }

	// // UPDATE /todolist/{id}
	// public ResponseEntity updateToDoItem(@RequestBody ToDoItem toDoItem, @PathVariable int id) {
	// 	try {
	// 		ToDoItem toDoItem1 = toDoItemService.updateToDoItem(id, toDoItem);
	// 		System.out.println(toDoItem1.toString());
	// 		return new ResponseEntity<>(toDoItem1, HttpStatus.OK);
	// 	} catch (Exception e) {
	// 		logger.error(e.getLocalizedMessage(), e);
	// 		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	// 	}
	// }

	// // DELETE todolist/{id}
	// public ResponseEntity<Boolean> deleteToDoItem(@PathVariable("id") int id) {
	// 	Boolean flag = false;
	// 	try {
	// 		flag = toDoItemService.deleteToDoItem(id);
	// 		return new ResponseEntity<>(flag, HttpStatus.OK);
	// 	} catch (Exception e) {
	// 		logger.error(e.getLocalizedMessage(), e);
	// 		return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
	// 	}
	// }
}

// if (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())
// 					|| messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel())) {

// 				SendMessage messageToTelegram = new SendMessage();
// 				messageToTelegram.setChatId(chatId);
// 				messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

// 				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
// 				List<KeyboardRow> keyboard = new ArrayList<>();

// 				// first row
// 				KeyboardRow row = new KeyboardRow();
// 				row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
// 				row.add(BotLabels.ADD_NEW_ITEM.getLabel());
// 				// Add the first row to the keyboard
// 				keyboard.add(row);

// 				// second row
// 				row = new KeyboardRow();
// 				row.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
// 				row.add(BotLabels.HIDE_MAIN_SCREEN.getLabel());
// 				keyboard.add(row);

// 				// Set the keyboard
// 				keyboardMarkup.setKeyboard(keyboard);

// 				// Add the keyboard markup
// 				messageToTelegram.setReplyMarkup(keyboardMarkup);

// 				try {
// 					execute(messageToTelegram);
// 				} catch (TelegramApiException e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			} else if (messageTextFromTelegram.indexOf(BotLabels.DONE.getLabel()) != -1) {

// 				String done = messageTextFromTelegram.substring(0,
// 						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
// 				Integer id = Integer.valueOf(done);

// 				try {

// 					ToDoItem item = getToDoItemById(id).getBody();
// 					item.setDone(true);
// 					updateToDoItem(item, id);
// 					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DONE.getMessage(), this);

// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			} else if (messageTextFromTelegram.indexOf(BotLabels.UNDO.getLabel()) != -1) {

// 				String undo = messageTextFromTelegram.substring(0,
// 						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
// 				Integer id = Integer.valueOf(undo);

// 				try {

// 					ToDoItem item = getToDoItemById(id).getBody();
// 					item.setDone(false);
// 					updateToDoItem(item, id);
// 					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_UNDONE.getMessage(), this);

// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			} else if (messageTextFromTelegram.indexOf(BotLabels.DELETE.getLabel()) != -1) {

// 				String delete = messageTextFromTelegram.substring(0,
// 						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
// 				Integer id = Integer.valueOf(delete);

// 				try {

// 					deleteToDoItem(id).getBody();
// 					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DELETED.getMessage(), this);

// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			} else if (messageTextFromTelegram.equals(BotCommands.HIDE_COMMAND.getCommand())
// 					|| messageTextFromTelegram.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel())) {

// 				BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);

// 			} else if (messageTextFromTelegram.equals(BotCommands.TODO_LIST.getCommand())
// 					|| messageTextFromTelegram.equals(BotLabels.LIST_ALL_ITEMS.getLabel())
// 					|| messageTextFromTelegram.equals(BotLabels.MY_TODO_LIST.getLabel())) {

// 				List<ToDoItem> allItems = getAllToDoItems();
// 				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
// 				List<KeyboardRow> keyboard = new ArrayList<>();

// 				// command back to main screen
// 				KeyboardRow mainScreenRowTop = new KeyboardRow();
// 				mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
// 				keyboard.add(mainScreenRowTop);

// 				KeyboardRow firstRow = new KeyboardRow();
// 				firstRow.add(BotLabels.ADD_NEW_ITEM.getLabel());
// 				keyboard.add(firstRow);

// 				KeyboardRow myTodoListTitleRow = new KeyboardRow();
// 				myTodoListTitleRow.add(BotLabels.MY_TODO_LIST.getLabel());
// 				keyboard.add(myTodoListTitleRow);

// 				List<ToDoItem> activeItems = allItems.stream().filter(item -> item.isDone() == false)
// 						.collect(Collectors.toList());

// 				for (ToDoItem item : activeItems) {

// 					KeyboardRow currentRow = new KeyboardRow();
// 					currentRow.add(item.getDescription());
// 					currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.DONE.getLabel());
// 					keyboard.add(currentRow);
// 				}

// 				List<ToDoItem> doneItems = allItems.stream().filter(item -> item.isDone() == true)
// 						.collect(Collectors.toList());

// 				for (ToDoItem item : doneItems) {
// 					KeyboardRow currentRow = new KeyboardRow();
// 					currentRow.add(item.getDescription());
// 					currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.UNDO.getLabel());
// 					currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.DELETE.getLabel());
// 					keyboard.add(currentRow);
// 				}

// 				// command back to main screen
// 				KeyboardRow mainScreenRowBottom = new KeyboardRow();
// 				mainScreenRowBottom.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
// 				keyboard.add(mainScreenRowBottom);

// 				keyboardMarkup.setKeyboard(keyboard);

// 				SendMessage messageToTelegram = new SendMessage();
// 				messageToTelegram.setChatId(chatId);
// 				messageToTelegram.setText(BotLabels.MY_TODO_LIST.getLabel());
// 				messageToTelegram.setReplyMarkup(keyboardMarkup);

// 				try {
// 					execute(messageToTelegram);
// 				} catch (TelegramApiException e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			} else if (messageTextFromTelegram.equals(BotCommands.ADD_ITEM.getCommand())
// 					|| messageTextFromTelegram.equals(BotLabels.ADD_NEW_ITEM.getLabel())) {
// 				try {
// 					SendMessage messageToTelegram = new SendMessage();
// 					messageToTelegram.setChatId(chatId);
// 					messageToTelegram.setText(BotMessages.TYPE_NEW_TODO_ITEM.getMessage());
// 					// hide keyboard
// 					ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
// 					messageToTelegram.setReplyMarkup(keyboardMarkup);

// 					// send message
// 					execute(messageToTelegram);

// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}

// 			}

// 			else {
// 				try {
// 					ToDoItem newItem = new ToDoItem();
// 					newItem.setDescription(messageTextFromTelegram);
// 					newItem.setCreation_ts(OffsetDateTime.now());
// 					newItem.setDone(false);
// 					ResponseEntity entity = addToDoItem(newItem);

// 					SendMessage messageToTelegram = new SendMessage();
// 					messageToTelegram.setChatId(chatId);
// 					messageToTelegram.setText(BotMessages.NEW_ITEM_ADDED.getMessage());

// 					execute(messageToTelegram);
// 				} catch (Exception e) {
// 					logger.error(e.getLocalizedMessage(), e);
// 				}
// 			}