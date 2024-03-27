import java.util.concurrent.*;

public class ExceptionModule {
    private static final int MAX_RETRY_COUNT = 3;
    private static final int ACK_TIMEOUT = 5000; // 指定时间内没有收到ACK则进行重试
    private static final int CRASH_TIMEOUT = 15000; // 重试多次后没有收到ACK则认为设备发生了crash

    private ExecutorService executor;

    public ExceptionModule() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void processMessage(Message message) {
        // 发送指令给设备
        sendMessageToDevice(message);

        try {
            // 等待ACK和执行结果response
            boolean ackReceived = waitForACK();
            if (ackReceived) {
                boolean responseReceived = waitForResponse();
                if (!responseReceived) {
                    // 重试
                    retrySendMessage(message);
                }
            } else {
                // 重试
                retrySendMessage(message);
            }
        } catch (InterruptedException e) {
            // 处理中断异常
            e.printStackTrace();
        } catch (ExecutionException e) {
            // 处理执行异常
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private void sendMessageToDevice(Message message) {
        // 将消息发送给设备
        System.out.println("Sending message to device: " + message);
    }

    private boolean waitForACK() throws InterruptedException, ExecutionException {
        Future<Boolean> ackFuture = executor.submit(() -> {
            // 模拟监听设备返回的ACK
            Thread.sleep(2000); // 模拟等待ACK的时间
            return true; // 如果收到ACK，返回true
        });

        try {
            return ackFuture.get(ACK_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            return false; // 指定时间内没有收到ACK
        }
    }

    private boolean waitForResponse() throws InterruptedException, ExecutionException {
        Future<Boolean> responseFuture = executor.submit(() -> {
            // 模拟监听设备返回的response
            Thread.sleep(2000); // 模拟等待response的时间
            return true; // 如果收到response，返回true
        });

        try {
            return responseFuture.get(ACK_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            return false; // 指定时间内没有收到response
        }
    }

    private void retrySendMessage(Message message) throws InterruptedException, ExecutionException {
        int retryCount = 0;
        boolean ackReceived = false;

        while (retryCount < MAX_RETRY_COUNT && !ackReceived) {
            // 重新发送指令给设备
            sendMessageToDevice(message);

            Future<Boolean> ackFuture = executor.submit(() -> {
                // 模拟监听设备返回的ACK
                Thread.sleep(2000); // 模拟等待ACK的时间
                return true; // 如果收到ACK，返回true
            });

            try {
                ackReceived = ackFuture.get(ACK_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                retryCount++;
            }
        }

        if (!ackReceived) {
            // 设备发生了crash，通知用户
            notifyUser();
        }
    }

    private void notifyUser() {
        // 模拟通知用户设备发生了crash
        System.out.println("Device has crashed. Notifying user...");
    }

    public static void main(String[] args) {
        ExceptionModule exceptionModule = new ExceptionModule();
        exceptionModule.processMessage(new Message("Hello, device!"));
    }
}

class Message {
    private String content;

    public Message(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}