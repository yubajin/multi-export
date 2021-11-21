package cn.yubajin.multiexport;

import cn.yubajin.multiexport.entity.User;
import cn.yubajin.multiexport.service.UserService;
import cn.yubajin.multiexport.utils.AccountUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class MultiExportApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    /****
     * 执行线程池里面的execute方法后，为啥线程不执行呢
     * run里面的打印日志都没打印啊？？？
     */
    @Test
    void testExecutorExecute(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            // 异步执行, submit伪返  他的get返回方法是阻塞的
            Runnable run = () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("this is Thread" + finalI);
            };
            executorService.execute(run);

        }

        if (executorService.isShutdown()){
            System.out.println("线程池关闭");
        }
        System.out.println("this is main Thread running");
    }

    /****
     * 光执行线程池里面的submit方法后，为啥线程不执行呢
     * 调用future的方法后才执行线程池里面的线程
     * run里面的打印日志都没打印啊
     */
    @Test
    void testExecutorSubmit(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            // 异步执行, submit伪返  他的get返回方法是阻塞的
            Future<?> submit = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("this is Thread" + finalI);
                }
            });
            try {
                System.out.println("this is Thread" + finalI + "has finished!!! and result is " + submit.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        System.out.println("this is main Thread running");
    }

    @Test
    void testExecutorInvokeAll(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            // 异步执行, submit伪返  他的get返回方法是阻塞的
            tasks.add(()-> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("this is Thread" + finalI);
                return 1;
            });
        }
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("this is main Thread running");
    }

    @Test
    public void testBatchInsert(){
        List list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setNickname(AccountUtil.getStringRandom(10));
            Random random = new Random();
            user.setPoint(random.nextInt(9));
            user.setLoginip("182.254.171.122");
            user.setOrgcode("5253273");
            list.add(user);
        }
        int update = userService.batchInsertUser(list);
        assert update > 0;
    }

    @Test
    public void testCreateUserUtils() {
        // 经过测试 1百万条数据需 83s
        // 导入70w数据
        // 35个批次 2w  70个线程
        ExecutorService pool = Executors.newFixedThreadPool(70);
        int totalNo = 50;
        int pageSize = 20000;
        AtomicInteger atomicInteger = new AtomicInteger();
        long start = System.currentTimeMillis();
        for (int i = 0; i < totalNo; i++) {
            CreateUserThread createUserThread = new CreateUserThread(pageSize);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Future<Integer> submit = pool.submit(createUserThread);
            try {
                atomicInteger.getAndAdd(submit.get());
                if (atomicInteger.get() == totalNo * pageSize){
                    System.out.println("数据同步完成，耗时: " + (System.currentTimeMillis() - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        pool.shutdown();
    }

    class CreateUserThread implements Callable {

        private int pageSize;

        public CreateUserThread(int pageSize) {
            this.pageSize = pageSize;
        }

        @Override
        public Integer call() throws Exception{

            List list = new ArrayList<>();
            for (int i = 0; i < pageSize; i++) {
                User user = new User();
                user.setNickname(AccountUtil.getStringRandom(10));
                Random random = new Random();
                user.setPoint(random.nextInt(9));
                user.setLoginip("182.254.171.122");
                user.setOrgcode("5253273");
                list.add(user);
            }
            int update = userService.batchInsertUser(list);
            return update;
        }

    }

}
