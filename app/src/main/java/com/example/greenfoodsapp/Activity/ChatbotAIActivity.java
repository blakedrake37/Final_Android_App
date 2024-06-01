package com.example.greenfoodsapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.greenfoodsapp.R;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executor;

import okhttp3.OkHttpClient;

// Ông Vũ Hữu Tài - 21110796
public class ChatbotAIActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện và các đối tượng cần thiết
    private LinearLayout chatLayout;
    private EditText chatInput;
    private ImageButton sendButton;
    private ScrollView scrollView;
    private OkHttpClient client;
    private Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_aiactivity);

        // Khởi tạo các thành phần giao diện
        chatLayout = findViewById(R.id.chatLayout);
        chatInput = findViewById(R.id.chatInput);
        sendButton = findViewById(R.id.sendButton);
        if (sendButton != null) {
            sendButton.setOnClickListener(view -> {
                // Hành động khi nhấn nút
                Log.d("ChatbotAIActivity", "Send button clicked");
            });
        } else {
            Log.e("ChatbotAIActivity", "ImageButton sendButton not found in layout");
        }
        scrollView = findViewById(R.id.scrollView);
        client = new OkHttpClient();
        executor = MoreExecutors.directExecutor(); // Hoặc bất kỳ executor nào phù hợp

        // Thiết lập sự kiện click cho nút sendButton
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = chatInput.getText().toString();
                if (!inputText.isEmpty()) {
                    addMessage(inputText, true); // Thêm tin nhắn của người dùng vào giao diện
                    sendMessageToGemini(inputText); // Gửi tin nhắn đến API Gemini
                    chatInput.setText(""); // Xóa nội dung trong EditText sau khi gửi
                }
            }
        });
    }

    // Hàm để thêm tin nhắn vào giao diện
    private void addMessage(String message, boolean isUser) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16);
        textView.setPadding(16, 16, 16, 16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 8);
        if (isUser) {
            params.gravity = Gravity.END;
            textView.setBackgroundResource(R.drawable.user_message_background);
            textView.setTextColor(getResources().getColor(R.color.white));
        } else {
            params.gravity = Gravity.START;
            textView.setBackgroundResource(R.drawable.bot_message_background);
            textView.setTextColor(getResources().getColor(R.color.black));
        }
        textView.setLayoutParams(params);
        chatLayout.addView(textView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN)); // Cuộn xuống dưới cùng
    }

    // Hàm để gửi tin nhắn đến API Gemini
    private void sendMessageToGemini(String message) {
        // Khởi tạo GenerativeModel và Content
        // Load models bằng API
        GenerativeModel gm = new GenerativeModel("gemini-pro", "AIzaSyCG0KFWeoVQBUHQ76t-b0U-NA2IE8sl_Mw");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(message)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                final String resultText = result.getText();
                runOnUiThread(() -> addMessage(resultText, false)); // Thêm tin nhắn của bot vào giao diện
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                runOnUiThread(() -> addMessage("Lỗi khi gửi tin nhắn: " + t.getMessage(), false)); // Hiển thị lỗi nếu gửi tin nhắn thất bại
            }
        }, executor);
    }
}
