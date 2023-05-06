package com.lc.netty.MulitThreads;

import com.google.gson.internal.$Gson$Preconditions;

/**
 * @Author Lc
 * @Date 2023/5/6
 * @Description
 */
public  class  Work1Thread {

    private static class woker{
        private static Thread Instance;
            private static Thread Thread(Runnable R,String name){
                return Instance = new Thread(R, name);
            }
    }

    public static Thread creatsingleton(Runnable R,String name) {
        return woker.Thread(R,name);
    }
}
