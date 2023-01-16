package com.example.bluegit;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bluegit.model.Message;
import com.example.bluegit.model.Order;
import com.example.bluegit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NotificationService extends Service {
    public static final String CHANNEL_ID = "channel1";
    FirebaseFirestore db;
    FireStoreManager fireStoreManager;
    String uid;
    Context context;
    int oldSellerOrderAmount;
    int oldChatWithAmount;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        db = FirebaseFirestore.getInstance();
        fireStoreManager = new FireStoreManager(getApplicationContext(),
                FirebaseAuth.getInstance().getCurrentUser());
        context = getApplicationContext();

        Intent notificationIntent = new Intent(this, LoginActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bluegit_logo)
                .setContentTitle("BlueGit")
                .setContentText("App is running in the background")
                .setContentIntent(pendingIntent).build();

        startForeground(12345, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        oldSellerOrderAmount = -1;
        oldChatWithAmount = -1;
        Log.d("TESTING", "call onStartCommand");

        uid = intent.getStringExtra("uid");

//        Listen for new chat
        AtomicBoolean isFirstNewChatListener = new AtomicBoolean(true);
        db.collection("users").document(uid)
            .collection("chatWith").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(oldChatWithAmount==-1){
                        oldChatWithAmount = 0;
                    } else if(value!=null){
                        if(!value.isEmpty() && value.size() > oldChatWithAmount){
                            oldChatWithAmount = value.size();
//                            stopService(intent);
                            startForegroundService(intent);
                        }
                    }
                }
            });

//        Chats listeners
        fireStoreManager.getChatWithOfAUser(new FireStoreManager.GetChatWithOfAUserCallBack() {
            @Override
            public void onSuccess(ArrayList<DocumentReference> chatRefList) {
                for(DocumentReference chatRef: chatRefList){
                    AtomicBoolean isFirstListener = new AtomicBoolean(true);

                    chatRef.collection("messages")
                            .orderBy("sentTime")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (isFirstListener.get()) {
                                isFirstListener.set(false);
                                return;
                            }

                            if(value!=null) {
                                if (!value.getDocuments().isEmpty()) {

                                    Message newMsg = value.getDocuments()
                                            .get(value.getDocuments().size() - 1)
                                            .toObject(Message.class);

                                    if (!newMsg.getFromId().equals(uid)) {
                                        fireStoreManager.getUserById(newMsg.getFromId(), new FireStoreManager.GetUserDataCallBack() {
                                            @Override
                                            public void onSuccess(User result) {
                                                String title = "New message";
                                                String message = "You have new message from " + result.getDisplayName();
                                                Intent intent = new Intent(context, ChatListActivity.class);
                                                sendNotification(title, message, intent);
                                            }

                                            @Override
                                            public void onFailure(Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

//        New sell order listener
        db.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null) {
                    User currentUser = value.toObject(User.class);
                    if (oldSellerOrderAmount == -1) {
                        oldSellerOrderAmount = currentUser.getSellOrderRef().size();
                    } else if (!currentUser.getSellOrderRef().isEmpty() && currentUser.getSellOrderRef().size() > oldSellerOrderAmount) {
                        oldSellerOrderAmount = currentUser.getSellOrderRef().size();
                        String title = "New order";
                        String message = "You have a new order";
                        Intent intent = new Intent(context, OrdersSellActivity.class);
                        sendNotification(title, message, intent);
                    }
                }
            }
        });

//        Buy orders status listener
        fireStoreManager.getBuyOrdersRef(new FireStoreManager.getOrdersRefCallBack() {
            @Override
            public void onSuccess(ArrayList<DocumentReference> result) {
                if(result!=null) {
                    for (DocumentReference orderRef : result) {
                        AtomicBoolean isFirstListener = new AtomicBoolean(true);

                        orderRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value != null) {

                                    if (isFirstListener.get()) {
                                        isFirstListener.set(false);
                                        return;
                                    }

                                    String title = "Order status update";
                                    String message = "One of your order's status is updated";
                                    Intent intent = new Intent(context, OrdersBuyActivity.class);
                                    sendNotification(title, message, intent);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        return START_REDELIVER_INTENT;
    }

    private void sendNotification(String title, String message, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.bluegit_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify((int) Math.round(Math.random()*100), builder.build());
    }

    private void createNotificationChannel() {
        CharSequence name = "channel1";
        String description = "this is channel1";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
