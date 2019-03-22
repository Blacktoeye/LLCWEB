package llcweb.com.controller.admin;

import llcweb.com.dao.repository.FileRepository;
import llcweb.com.dao.repository.UsersRepository;
import llcweb.com.domain.entity.UsefulFile;
import llcweb.com.domain.models.File;
import llcweb.com.domain.models.Users;
import llcweb.com.exception.BusinessException;
import llcweb.com.service.FileService;
import llcweb.com.service.UsersService;
import llcweb.com.tools.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author haien
 * @Description 文件控制器
 * @Date 22:05 2018/9/5
 **/
@RestController
@RequestMapping("/file")
public class FileController {
    private final static Logger logger=LoggerFactory.getLogger(FileController.class);

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileRepository fileRepository;

    /**
     * @Author haien
     * @Description 更新/添加文件
     * @Date 2018/9/5
     * @Param [image]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @PostMapping("/save")
    @Transactional
    public Map<String,Object> saveFile(@RequestParam("file")MultipartFile multipartFile,
                                        @RequestParam("group")String group,
                                        @RequestParam("introduction")String introduction,
                                        @RequestParam(value="id",required = false)Integer id){
        Map<String,Object> map=new HashMap<>();

        //验证文件是否为空
        if(multipartFile.isEmpty() || StringUtil.isNull(multipartFile.getOriginalFilename())){
            map.put("result",0);
            map.put("message","文件为空！");
            return map;
        }

        /*保存文件*/
        //保存到数据库
            //添加
        File file=null;
        if (null == id||id.equals("")||id<0) {
            logger.info("添加文件：name=" + multipartFile.getOriginalFilename() + ",type=" + multipartFile.getContentType());
            file=new File();
        }
            //更新
        else if (id > 0) {
            logger.info("更新文件：id=" + id + "name=" + multipartFile.getOriginalFilename() + ",type=" + multipartFile.getContentType());
            file=fileRepository.findOne(id);
            if(file==null){
                map.put("result", 0);
                map.put("message", "找不到该文件！");
                return map;
            }
        } else {
            map.put("result", 0);
            map.put("message", "参数错误！");
            return map;
        }
        //获取当前用户
        Users user=usersService.getCurrentUser();
        String userName=user.getUsername();
        int userId=usersRepository.findByUsername(userName).getId();
        //保存数据
        if(StringUtil.isNull(introduction) || introduction.length()>50){
            map.put("result", 0);
            map.put("message", "请填写50字内的简介！");
            return map;
        }
        //填充字段
        file.setIntroduction(introduction);
        file.setCreateDate(new Date());
        file.setAuthor(userName);
        file.setAuthorId(userId);
        file.setModel(group);
        file.setOriginalName(multipartFile.getOriginalFilename());

        //获取数据库生成的id，用于生成唯一命名
        file = fileRepository.save(file);

        //保存到项目
        String path;
        //更新操作应当先删除原有文件
        if(id!=null&&id>0){
            path=file.getPath();
            //删除文件
            try {
                fileService.deleteResource(path);
            } catch (FileNotFoundException e) {
                map.put("result",0);
                map.put("message","项目中不存在该文件！");
                return map;
            } catch (BusinessException e) {
                e.printStackTrace();
                map.put("result",0);
                map.put("message","删除原文件失败！");
                return map;
            }
        }
        try {
            //返回文件路径
            path=fileService.saveResource(multipartFile,file);
        } catch (BusinessException e) {
            e.printStackTrace();
            map.put("result",0);
            map.put("message","上传失败，文件格式错误！");
            return map;
        }
        file.setPath(path); //保存文件路径
        fileRepository.save(file);
        return map;
    }

    /**
     * 删除文件
     */
    @RequestMapping("/delete")
    @Transactional
    public Map<String,Object> deleteFile(@RequestParam("id")Integer id){
        Map<String,Object> map=new HashMap<>();

        logger.info("删除文件：id="+id);

        File file=null;
        if(id != null && id>0){ //验证参数部分放在controller层，放在service层则删除项目文件、数据库记录分别需要一次验证，更复杂
            //删除项目文件
            file=fileRepository.findOne(id);
            if(file==null){
                map.put("result",0);
                map.put("message","找不到该文件！");
                return map;
            }
            try {
                fileService.deleteResource(file.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                map.put("result",0);
                map.put("message","找不到该文件！");
                return map;
            } catch (BusinessException e) {
                e.printStackTrace();
                map.put("result",0);
                map.put("message",e.getMessage());
                return map;
            }
            //删除数据库记录
            fileRepository.delete(id);
        }
        logger.info("--删除文件"+file.getPath()+"成功--");
        map.put("result",1);
        map.put("message","删除成功！");
        return map;
    }

    /**
     * 查找文件
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public Map<String,Object> page(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map =new HashMap<String,Object>();

        //直接返回前台
        String draw = request.getParameter("draw");
        //当前数据的起始位置 ，如第10条
        String startIndex = request.getParameter("startIndex");
        //数据长度
        String pageSize = request.getParameter("pageSize");
        int size = Integer.parseInt(pageSize);
        //模糊|高级查询标志
        String fuzzy = request.getParameter("fuzzySearch");
        if(draw==null||startIndex==null||pageSize==null||fuzzy==null){
            map.put("result", 0);
            map.put("message", "参数不完整！");
            return map;
        }
        //页码
        int currentPage = Integer.parseInt(startIndex)/size+1;
        Users user=usersService.getCurrentUser();

        logger.info("size = "+size+",currentPage = "+currentPage);

        Page<File> filePage = null;
        //模糊查找
        if("true".equals(fuzzy)){
            String searchValue=request.getParameter("fuzzy");
            //空搜
            if(StringUtil.isNull(searchValue)){
                //日志
                logger.info("无关键词搜索--默认按权限获取文档");
                filePage=fileService.selectByRole(user,currentPage-1,size,fileRepository);
            }
            else {
                //日志
                logger.info("模糊查询---关键词："+searchValue);
                Pageable pageable = new PageRequest(currentPage - 1, size, Sort.Direction.DESC, "date");
                filePage = fileRepository.fuzzySearch(searchValue, pageable);
            }
        }
        //高级查找
        else{
            String author=request.getParameter("author");
            String introduction=request.getParameter("introduction");
            String model=request.getParameter("model");
            String firstDate1=request.getParameter("firstDate");
            String lastDate1=request.getParameter("lastDate");
            //字符串对象转为日期对象
            Date firstDate=null;
            Date lastDate=null;
            try {
                if(StringUtil.isNull(firstDate1)){
                    firstDate=new SimpleDateFormat("yyyy-MM-dd").parse(firstDate1);
                }
                if(StringUtil.isNull(lastDate1)) {
                    lastDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastDate1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                map.put("draw", draw);
                map.put("result", 0);
                map.put("message", "日期格式错误！");
                return map;
            }

            //空搜
            if(StringUtil.isNull(author)&&StringUtil.isNull(introduction)&&StringUtil.isNull(model)&&StringUtil.isNull(firstDate1)&&StringUtil.isNull(lastDate1)){
                filePage=fileService.selectByRole(user,currentPage-1,size,fileRepository);
            }
            else {
                //日志
                logger.info("---高级查询---");
                UsefulFile file = new UsefulFile(user.getUsername(), model, introduction, firstDate, lastDate);
                filePage = fileService.activeSearch(file, currentPage - 1, size, fileRepository);
            }
        }

        //总记录数
        long total = filePage.getTotalElements();
        logger.info("total="+total);

        map.put("draw", draw);
        map.put("result", 1);
        if(0==total){
            map.put("message", "未查询到文件！");
        }else {
            map.put("total", total);
            map.put("pageData", filePage.getContent());
            map.put("message", "成功获取分页数据！");
        }
        return map;
    }

    /**
     * 获取文件链接
     **/
    @RequestMapping("/getPath")
    public Map<String,Object> loadFile(@RequestParam("id")Integer id,HttpServletResponse response){
        Map<String,Object> map=new HashMap<>();
        logger.info("获取文件链接：id="+id);

        if(null==id||id<=0){
            map.put("result",0);
            map.put("message","参数错误！");
            return map;
        }

        //获取文件
        File file=fileRepository.findOne(id);
        if(file==null){
            map.put("result",0);
            map.put("message","找不到文件！");
        }else{ //获取文件输出流
            try {
                fileService.getOutputStream(file.getPath(),response);
            } catch (FileNotFoundException e) {
                map.put("result",0);
                map.put("message","找不到该文件！");
            } catch (IOException e) {
                map.put("result",0);
                map.put("message","格式错误！");
            }
            map.put("result",1);
            map.put("message","成功获取文件！");
        }
        return map;
    }


    /**
     * @Author haien
     * @Description 前端请求下载Excel表格，返回一个输出流
     * @Date 11:34 2018/7/22
     * @Param [request, response]
     * @return void
     **/
    @RequestMapping(value = "/getFileById")
    @ResponseBody
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, BusinessException {
        // TODO Auto-generated method stub
        //获得请求文件名
        String encoding = System.getProperty("file.encoding");
        String idStr = request.getParameter("id");
        if(idStr==null||idStr.isEmpty()){
            throw new BusinessException(404,"file not find!");
        }
        //String enFileName = URLEncoder.encode(filename,"utf-8");
        System.out.println("/getFileById id="+idStr);
        int id = Integer.parseInt(idStr);
        File file =  fileRepository.findOne(id);
        if(file!=null&&file.getPath()!=null){
            //设置Content-Disposition
            response.setHeader("Content-Disposition", "attachment;filename="+file.getOriginalName());
            //读取目标文件，通过response将目标文件写到客户端
            //读取文件
            String fileName = new String(file.getPath().getBytes("UTF-8"),encoding);
            InputStream in = new FileInputStream(fileName);
            OutputStream out = response.getOutputStream();
            //写文件
            int b;
            while((b=in.read())!= -1) {
                out.write(b);
            }
            in.close();
            out.close();
        }


    }


    /**
     * 获取Files 对象信息
     **/
    @RequestMapping("/getFile")
    public Map<String,Object> getFile(@RequestParam("id")Integer id,HttpServletResponse response){
        Map<String,Object> map=new HashMap<>();
        logger.info("获取文件信息：id="+id);

        if(null==id||id<=0){
            map.put("result",0);
            map.put("message","参数错误！");
            return map;
        }

        //获取文件
        File file=fileRepository.findOne(id);
        if(file==null){
            map.put("result",0);
            map.put("message","找不到文件！");
        }else{ //获取文件输出流
            map.put("data",file);
            map.put("result",1);
            map.put("message","成功获取文件！");
        }
        return map;
    }
}