package cn.yubajin.multiexport.controller;

import cn.yubajin.multiexport.common.annotation.Excel;
import cn.yubajin.multiexport.common.entity.ResponseDTO;
import cn.yubajin.multiexport.entity.User;
import cn.yubajin.multiexport.entity.dto.UserAddDTO;
import cn.yubajin.multiexport.entity.dto.UserDTO;
import cn.yubajin.multiexport.handler.MyPageHelper;
import cn.yubajin.multiexport.handler.UserHandler;
import cn.yubajin.multiexport.service.UserService;
import cn.yubajin.multiexport.tool.SmartBeanUtil;
import cn.yubajin.multiexport.tool.poi.ExcelUtil;
import cn.yubajin.multiexport.wrapper.UserWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * (User)表控制层
 *
 * @author yubj17
 * @since 2021-11-16 14:53:54
 */
@RestController
@RequestMapping("user")
@AllArgsConstructor
@Api(tags = "用户 yubj17")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;


    /**
     * 查询列表数据
     */
    @ApiOperation(value = "-查询列表数据")
    @PostMapping("/list")
    public ResponseDTO list(@RequestBody UserDTO userDTO)
    {
        User copy = SmartBeanUtil.copy(userDTO, User.class);
        PageHelper.startPage(userDTO.getPageNum(), userDTO.getPageSize());
        List<User> list = userService.queryAll(copy);
        return ResponseDTO.succData(UserWrapper.build().pageVO(list));
    }

    /**
     * 查询列表数据2
     * @param userDTO
     * @return
     */
    @ApiOperation(value = "-查询列表数据2")
    @PostMapping("/listUser")
    public ResponseDTO listUser(@RequestBody UserDTO userDTO)
    {
        User copy = SmartBeanUtil.copy(userDTO, User.class);
        PageHelper.startPage(userDTO.getPageNum(), userDTO.getPageSize());
        List<User> list = userService.queryList(copy);
        return ResponseDTO.succData(UserWrapper.build().pageVO(list));
    }

    @ApiOperation(value = "-导出数据2")
    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Integer choose) throws IOException
    {
        int tablePerSize = 60000;

        /***************************/
        long start = System.currentTimeMillis();
        //100w条数据直接全部查询， 单线程写到一张table, 多个sheet中，一个sheet最多存放65536调数据， 85s
        if (choose == 1) {
            ExcelUtil<User> util = new ExcelUtil<User>(User.class);
            start = System.currentTimeMillis();
            List<User> list = userService.lambdaQuery().list();
            util.exportExcel(list, "用户数据");

            System.out.println("不分页查询单线程耗时--------------->>>" +  (System.currentTimeMillis() - start));
        }else
        //100w条数据直接全部查询， 多线程写到一张table, 多个sheet中，一个sheet最多存放65536调数据， 89s
        if (choose == 2) {
            int count = userService.count(Wrappers.query());
            int index = (int) Math.ceil(count * 1.0 / tablePerSize);
            ExcelUtil<User> util = new ExcelUtil<User>(User.class);
            start = System.currentTimeMillis();
            List<User> list = userService.lambdaQuery().list();  // 直接进入老年代吧

            util.init(list, "用户数据", Strings.EMPTY, Excel.Type.EXPORT);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File("E:\\export\\export.xlsx")));

            int threadNum = index;
            List<Callable<Integer>> tasks = new ArrayList<>();
            final int finalTablePerSize = tablePerSize;
            final int finalIndex = index;
            tasks.add(() -> {
                for (int i = 0; i < finalIndex; i++) {
                    List<User> subList = list.subList(i * finalTablePerSize, Math.min((i + 1) * finalTablePerSize, list.size()));
                    util.initSheet(subList, i);
                    util.fillSheet(i);
                }
                // return 无实际意义
                return 1;
            });
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
            try {
                List<Future<Integer>> futures = threadPoolExecutor.invokeAll(tasks);
                threadPoolExecutor.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            util.exportExcelSheet(bufferedOutputStream);
            System.out.println("不分页查询多线程批量查询耗时--------------->>>" +  (System.currentTimeMillis() - start));
        }else
        //100w条数据分页的方式分批查询， 多线程写到一张table, 多个sheet中，  302s
        if (choose == 3) {
            ExcelUtil<User> util = new ExcelUtil<User>(User.class);
            tablePerSize = 10000;
            int count = userService.count(Wrappers.query());
            int index = (int) Math.ceil(count * 1.0 / tablePerSize);
            util.initWorkBook("用户数据");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File("E:\\export\\export.xlsx")));

            int threadNum = index;
            List<Callable<Integer>> tasks = new ArrayList<>();
            final int finalTablePerSize = tablePerSize;
            final int finalIndex = index;
            tasks.add(() -> {
                for (int i = 0; i < finalIndex; i++) {
                    int pageNum = i * finalTablePerSize + 1;
                    int pageSize = Math.min(finalTablePerSize, count - pageNum + 1);
                    List<User> list = userService.queryAllByLimit(pageNum - 1, pageSize);
                    util.initSheet(list, i);
                    util.fillSheet(i);
                }
                // return 无实际意义
                return 1;
            });
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
            try {
                List<Future<Integer>> futures = threadPoolExecutor.invokeAll(tasks);
                threadPoolExecutor.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            util.exportExcelSheet(bufferedOutputStream);
            System.out.println("分页批量查询多线程批量查询耗时--------------->>>" +  (System.currentTimeMillis() - start));
        }else
        // 100w条数据分页的方式分批查询， 多线程写到多张table 66s
        if (choose == 4){
            tablePerSize = 10000;
            int count = userService.count(Wrappers.query());
            int index = (int) Math.ceil(count * 1.0 / tablePerSize);

            int threadNum = index;
            List<Callable<Integer>> tasks = new ArrayList<>();
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(threadNum / 2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
            final int finalTablePerSize = tablePerSize;
            final int finalIndex = index;
            long finalStart = start;
            AtomicInteger atomicInteger = new AtomicInteger();
            for (int i = 0; i < finalIndex; i++){
                final int finalI = i;
                tasks.add(() -> {
                    String tableName = "用户数据(" + finalI + ")";
                    String sheetName = "用户数据";
                    int pageNum = finalI * finalTablePerSize + 1;
                    int pageSize = Math.min(finalTablePerSize, count - pageNum + 1);
                    List<User> list = userService.queryAllByLimit(pageNum - 1, pageSize);
                    try {
                        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
                        util.exportExcel(list, sheetName, tableName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    atomicInteger.getAndAdd(list.size());
                    if (atomicInteger.get() == count){
                        System.out.println("数据导出完成，耗时： " + (System.currentTimeMillis() - finalStart));
                    }
                    return list.size();
                });
            }
            try {
                threadPoolExecutor.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }else
        // 100w条数据直接全部查询， 多线程写到多张table  69s
        if (choose == 5){
            tablePerSize = 10000;
            int count = userService.count(Wrappers.query());
            int index = (int) Math.ceil(count * 1.0 / tablePerSize);

            List<Callable<Integer>> tasks = new ArrayList<>();

            int threadNum = index;
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(threadNum / 2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
            // 直接进入老年代吧 oom问题， jvm内存相关知识
            // jvm调优流程
            List<User> list = userService.lambdaQuery().list();

            final int finalTablePerSize = tablePerSize;
            final int finalIndex = index;
            long finalStart = start;
            AtomicInteger atomicInteger = new AtomicInteger();
            for (int i = 0; i < finalIndex; i++){
                int finalI = i;
                tasks.add(() -> {
                    String tableName = "用户数据(" + finalI + ")";
                    String sheetName = "用户数据";
                    List<User> subList = list.subList(finalI * finalTablePerSize, Math.min((finalI + 1) * finalTablePerSize, list.size()));
                    try {
                        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
                        util.exportExcel(subList, sheetName, tableName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    atomicInteger.getAndAdd(list.size());
                    if (atomicInteger.get() == (count)){
                        System.out.println("数据导出完成，耗时： " + (System.currentTimeMillis() - finalStart));
                    }
                    return list.size();
                });
            }

            try {
                threadPoolExecutor.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("end-------->直接全部多线程查询, 导出到多表单个sheet 耗时--------------->>>" +  (System.currentTimeMillis() - start));
        }else
        // 原生jdbc limit 批量查询， 多线程写到多张table, 重写excel导出工具类  23s
        if (choose == 6){
            tablePerSize = 10000;
            //获取总条数
            int count = UserHandler.queryCount();
            int index = (int) Math.ceil(count * 1.0 / tablePerSize);

            int threadNum = index;
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(threadNum / 2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

            final int finalTablePerSize = tablePerSize;
            final int finalIndex = index;
            long finalStart = start;
            AtomicInteger atomicInteger = new AtomicInteger();
            for (int i = 0; i < finalIndex; i++){
                int finalI = i;
                Runnable task = () -> {
                    long startTask = System.currentTimeMillis();
                    String pageSql = MyPageHelper.getPageSql(finalI * finalTablePerSize, finalTablePerSize);
                    List list = UserHandler.queryUserList(pageSql);
                    System.out.println(pageSql + " 耗时： " + (System.currentTimeMillis() - startTask));
                    ExcelUtil.CreateExcel(finalI, list);
                    System.out.println(finalI + "线程创建excel耗时： " + (System.currentTimeMillis() - startTask));
                    atomicInteger.addAndGet(list.size());
                    if (atomicInteger.get() == (count)){
                        System.out.println("数据导出完成，耗时： " + (System.currentTimeMillis() - finalStart));
                    }
                };
                threadPoolExecutor.execute(task);
            }
        }
        /***************************/
    }
    
    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation(value = "-通过主键查询单条数据")
    @GetMapping("selectOne")
    public ResponseDTO selectOne(Long id) {
        return ResponseDTO.succData(UserWrapper.build().entityVO(this.userService.queryById(id)));
    }

    /**
     * 新增单条记录
     */
    @ApiOperation(value = "-新增单条记录")
    @PostMapping
    public ResponseDTO add(@RequestBody UserAddDTO userAddDTO)
    {
        return ResponseDTO.succData(userService.insertUser(userAddDTO));
    }

    /**
     * 修改单条记录
     */
    @ApiOperation(value = "-修改单条记录")
    @PutMapping
    public ResponseDTO edit(@RequestBody UserAddDTO userAddDTO)
    {
        return ResponseDTO.succData(userService.updateUser(userAddDTO));
    }

    /**
     * 根据id批量删除记录
     */
    @ApiOperation(value = "-根据id批量删除记录")
	@DeleteMapping("/{ids}")
    public ResponseDTO remove(@PathVariable Long[] ids)
    {
        return ResponseDTO.succData(userService.removeByIds(Arrays.asList(ids)));
    }
    
}