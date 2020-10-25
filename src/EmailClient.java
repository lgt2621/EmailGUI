import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.mail.internet.InternetAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Purpose: The GUI for the email client
 * Author: Liam Tyler
 */
public class EmailClient extends Application{

    private Scene emailScreen;
    private Scene logOn;
    private Scene homePage;
    private Scene repeatScreen;
    private Emailer emailer;
    private Scene scheduleScreen;
//     private String to;
//     private String from;
//     private String password;
//     private String message;
//     private String sub;
//     private Random rando=new Random();

    @Override
    public void start(Stage stage){
        intialize(stage);
        stage.setScene(logOn);
        stage.show();
//        Emailer email=new Emailer();
//        Button send=new Button("Send");
//        Button authenticate=new Button("Log in");
//        TextArea messenger=new TextArea();
//        TextField contact=new TextField();
//        TextField subject=new TextField();
//        TextField reciever=new TextField();
//        PasswordField passwordField=new PasswordField();
//        Scene emailScreen=buildEmailScreen(contact, messenger, send, subject);
//        send.setOnAction(e-> {
//            this.to = contact.getText();
//            this.message = messenger.getText();
//            this.sub=subject.getText();
//            subject.clear();
//            contact.clear();
//            messenger.clear();
//            stage.setScene(buildLogOn(reciever, passwordField, authenticate));
//        });
//        authenticate.setOnAction(e->{
//            this.from=reciever.getText();
//            this.password=passwordField.getText();
//            reciever.clear();
//            passwordField.clear();
//            for(int i=0; i<5; i++) {
//                email.sendMail(this.to, this.from, this.password, this.message, this.sub);
//                try {
//                    int sleepyTime=(rando.nextInt(50)*60000)+600000;
//                    System.out.println((sleepyTime/60000));
//                    Thread.sleep(sleepyTime);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//            }
//            stage.setScene(buildEmailScreen(contact, messenger, send, subject));
//        });
//        stage.setScene(emailScreen);
//        stage.show();

    }

    /**
     * Creates the screen where the user writes the email
     * @param stage(Stage) the programs window
     * @return (Scene) the newly made screen
     */
    private Scene buildEmailScreen(Stage stage){

        Label to=new Label("To: (If multiple recipients separate with commas)");
        to.setMaxWidth(Double.MAX_VALUE);
        to.setAlignment(Pos.CENTER_LEFT);

        Label sub=new Label("Subject:");
        sub.setMaxWidth(Double.MAX_VALUE);
        sub.setAlignment(Pos.CENTER_LEFT);

        Label directions=new Label("Please enter your message");
        directions.setMaxWidth(Double.MAX_VALUE);
        directions.setAlignment(Pos.CENTER_LEFT);

        TextField contact=new TextField();
        contact.setPromptText("To");

        TextField subject=new TextField();
        subject.setPromptText("Subject");

        TextArea messenger=new TextArea();

        Button send=new Button("Send");
        send.setMaxWidth(Double.MAX_VALUE);
        send.setOnAction(e->{
            try {
                emailer.setTo(contact.getText());
                emailer.setSubject(subject.getText());
                emailer.setBody(messenger.getText());
                emailer.send();
                stage.setScene(homePage);
            }catch (MailerException e1){
                error(e1);
            }finally {
                contact.clear();
                subject.clear();
                messenger.clear();
            }
        });

        Button cancle=new Button("Cancel");
        cancle.setMaxWidth(Double.MAX_VALUE);
        cancle.setOnAction(e->{
            contact.clear();
            subject.clear();
            messenger.clear();
            stage.setScene(homePage);
        });

        HBox buttons=new HBox();
        buttons.getChildren().addAll(send, cancle);
        buttons.setMaxWidth(Double.MAX_VALUE);
        for(Node node: buttons.getChildren()){
            buttons.setHgrow(node, Priority.ALWAYS);
        }

        VBox holder=new VBox();
        holder.getChildren().addAll(to, contact, sub, subject, directions, messenger, buttons);
        holder.setAlignment(Pos.CENTER);
        Scene emailScreen=new Scene(holder, 500, 500);
        return emailScreen;
    }

    /**
     * Creates the logon screen
     * @param stage(Stage) the program's window
     * @return (Scene) the logon screen
     */
    private Scene buildLogOn(Stage stage){

        Label username=new Label("Username: (up to @)");
        username.setMaxWidth(300);

        TextField from=new TextField();
        from.setPromptText("Your Email");
        from.setMaxWidth(300);

        Label pass=new Label("Password:");
        pass.setMaxWidth(300);

        PasswordField password=new PasswordField();
        password.setPromptText("Password");
        password.setMaxWidth(300);

        Button logIn=new Button("Log In");
        logIn.setOnAction(e-> {
            try {
                emailer.connect(from.getText(), password.getText());
                stage.setScene(homePage);
            } catch (MailerException e1) {
                error(e1);
            }finally {
                from.clear();
                password.clear();
            }
        });

        VBox holder=new VBox();
        holder.getChildren().addAll(username, from, pass, password, logIn);
        holder.setAlignment(Pos.CENTER);
        holder.setSpacing(10);
        Scene authenication=new Scene(holder,500, 500);
        return authenication;
    }

    /**
     * Builds the screen where the user will select which type of email to send
     * @param stage(Stage) the program's window
     * @return (Scene) the home screen
     */
    private Scene buildHomePage(Stage stage){

        Label title=new Label("A simple email client");
        title.setFont(new Font("Arial", 25));
        Label instructions=new Label("Please select the type of message you would like to send");

        Button singleMessage=new Button("Email");
        singleMessage.setMinWidth(150);
        singleMessage.setOnAction(e->{
            emailer.setType("Single");
            stage.setScene(emailScreen);
        });

        Button groupMessage=new Button(" Group Email");
        groupMessage.setMinWidth(150);
        groupMessage.setOnAction(e->{
            emailer.setType("Group");
            stage.setScene(emailScreen);
        });

        Button scheduledMessage=new Button("Schedule Email");
        scheduledMessage.setMinWidth(150);
        scheduledMessage.setOnAction(e->{
            emailer.setType("Scheduled");
            stage.setScene(scheduleScreen);
        });

        Button randomMessage=new Button("Random Email");
        randomMessage.setMinWidth(150);
        randomMessage.setOnAction(e->{
            stage.setScene(repeatScreen);
        });

        Button spam=new Button("Oh God Oh Fuck");
        spam.setMaxWidth(310);
        spam.setOnAction(e->{
            emailer.setType("Spam");
            stage.setScene(emailScreen);
        });

        Button logOff=new Button("Log Out");
        logOff.setMaxWidth(310);
        logOff.setOnAction(e->{
            try {
                emailer.close();
                stage.setScene(logOn);
            } catch (MailerException e1) {
                error(e1);
            }
        });

        HBox top =new HBox();
        top.getChildren().addAll(singleMessage, groupMessage);
        top.setMaxWidth(Double.MAX_VALUE);
        top.setAlignment(Pos.CENTER);
        top.setSpacing(10);

        HBox bottom=new HBox();
        bottom.getChildren().addAll(scheduledMessage, randomMessage);
        bottom.setMaxWidth(Double.MAX_VALUE);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(10);

        VBox holder =new VBox();
        holder.getChildren().addAll(title, instructions, top, bottom, spam, logOff);
        holder.setAlignment(Pos.CENTER);
        holder.setMaxWidth(Double.MAX_VALUE);
        holder.setSpacing(10);
        return new Scene(holder, 500, 500);
    }

    public Scene buildRepeat(Stage stage){
        Label instructions=new Label("How many times would you like the message to repeat");

        TextField number=new TextField();
        number.setMaxWidth(50);

        Button next=new Button("Continue");
        next.setOnAction(e->{
            String input=number.getText();
            try {
                int repetitions = Integer.parseInt(input);
                if(repetitions<=0){
                    error(new MailerException("Please enter a positive number"));
                }else{
                    emailer.setType("Random "+repetitions);
                    stage.setScene(emailScreen);
                }
            }catch(NumberFormatException nfe){
                error(new MailerException("Didn't enter a number. Please be reasonable"));
            }finally{
                number.clear();
            }
        });

        Button cancel=new Button("Cancel");
        cancel.setOnAction(e->{
            number.clear();
            stage.setScene(homePage);
        });

        VBox holder=new VBox();
        holder.getChildren().addAll(instructions, number, next, cancel);
        holder.setAlignment(Pos.CENTER);
        holder.setSpacing(5);

        return new Scene(holder, 500, 500);
    }

    public Scene buildSchedule(Stage stage){

        DatePicker datePicker=new DatePicker();
        Callback<DatePicker, DateCell> dayCellFactory =new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);
                        if(item.compareTo(LocalDate.now())<0){
                            setDisable(true);
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);

        TextField hours = new TextField();
        Label hDirections = new Label("Hours:");
        TextField minutes = new TextField();
        Label mDirections = new Label("Minutes:");
        ChoiceBox amPm = new ChoiceBox(FXCollections.observableArrayList("Am", "Pm"));
        Button next = new Button("Next");
        next.setOnAction(e->{
            LocalDate date = datePicker.getValue();
            long waitTime = calculateWaitTime(date, Integer.parseInt(hours.getText()),
                    Integer.parseInt(minutes.getText()),
                    amPm.getValue().equals("Pm"));

            emailer.setType("Scheduled " + waitTime);
            stage.setScene(emailScreen);

            hours.clear();
            minutes.clear();
        });

        HBox hour=new HBox();
        hour.getChildren().addAll(hDirections, hours);
        HBox minute=new HBox();
        minute.getChildren().addAll(mDirections, minutes);
        VBox holder=new VBox();
        holder.getChildren().addAll(datePicker, hour, minute, amPm, next);
        return new Scene(holder, 500, 500);


        //Date date=datePicker.getValue();
    }

    private long calculateWaitTime(LocalDate date, int hours, int minutes, boolean afternoon){
        long sendTime = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(sendTime);
        if(afternoon){
            sendTime +=((hours + 12) * 3600000) + (minutes *60000);
        }else{
            sendTime += (hours * 3600000) + (minutes * 60000);
        }

        long waitTime = sendTime - System.currentTimeMillis();
        System.out.println(sendTime);
        System.out.println(System.currentTimeMillis());
        if(waitTime <0){
          //TODO error case
        }
        return waitTime;
    }

    /**
     * Psuedo constructor. Instantiates emailer and builds all the screens
     * @param stage(Stage) the program's window
     */
    public void intialize(Stage stage){
        this.emailer=new Emailer();
        this.emailScreen=buildEmailScreen(stage);
        this.homePage=buildHomePage(stage);
        this.logOn=buildLogOn(stage);
        this.repeatScreen=buildRepeat(stage);
        this.scheduleScreen=buildSchedule(stage);
    }

    /**
     * Builds a new window that displays an error message
     * @param except(MailerException) the exception that will be reported
     */
    private void error(Exception except){
        Stage errorWindow=new Stage();
        Label errorMessage=new Label(except.getMessage());
        errorMessage.setAlignment(Pos.CENTER);
        errorMessage.setWrapText(true);
        Scene errorScreen=new Scene(errorMessage, 300, 50);
        errorWindow.setScene(errorScreen);
        errorWindow.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
