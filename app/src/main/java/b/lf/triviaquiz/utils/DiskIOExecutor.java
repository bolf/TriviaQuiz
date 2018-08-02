package b.lf.triviaquiz.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskIOExecutor {
    private static final Object LOCK = new Object();
    private static DiskIOExecutor sInstance;
    private final Executor diskIO;

    private DiskIOExecutor(Executor diskIO){
        this.diskIO = diskIO;
    }

    public static DiskIOExecutor getInstance(){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new DiskIOExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO(){
        return diskIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
