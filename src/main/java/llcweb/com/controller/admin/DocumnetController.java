
package llcweb.com.controller.admin;

import llcweb.com.dao.repository.DocumentRepository;
import llcweb.com.domain.entities.DocumentInfo;
import llcweb.com.domain.entity.UsefulDocument;
import llcweb.com.domain.models.Document;
import llcweb.com.domain.models.Users;
import llcweb.com.service.DocumentService;
import llcweb.com.service.UsersService;
import llcweb.com.tools.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@Author: Ricardo
 *@Description: 此文件用于文档类的数据接口
 *@Date: 15:14 2018/8/21
 **/
@Controller
@RequestMapping("/document")
public class DocumnetController {
    private static final Logger logger = LoggerFactory.getLogger(DocumnetController.class);

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private UsersService usersService;

    /**
     * 模糊查找
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> page(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map =new HashMap<String,Object>();

        //直接返回前台
        String draw = request.getParameter("draw");
        //当前数据的起始位置
        String startIndex = request.getParameter("startIndex");
        //数据长度
        String pageSize = request.getParameter("pageSize");
        //模糊|高级查询标志
        String fuzzy = request.getParameter("fuzzySearch");
        if(draw==null||startIndex==null||pageSize==null){
            map.put("result", 0);
            map.put("message", "参数不完整！");
            return map;
        }
        int size = Integer.parseInt(pageSize);
        //页码
        int currentPage = Integer.parseInt(startIndex)/size+1;
        Users user=usersService.getCurrentUser();

        logger.info("size = "+size+",currentPage = "+currentPage);

        Page<Document> documentPage = null;
        //模糊查找
        if("true".equals(fuzzy)){
            String searchValue=request.getParameter("fuzzy");
            //空搜
            if(StringUtil.isNull(searchValue)){
                //日志
                logger.info("无关键词搜索--默认按权限获取文档");
                documentPage=documentService.selectByRole(user,currentPage-1,size,documentRepository);
            }
            else{
                //日志
                logger.info("模糊查询---关键词："+searchValue);
                Pageable pageable=new PageRequest(currentPage-1,size, Sort.Direction.DESC,"createDate");
                documentPage = documentRepository.fuzzySearch(searchValue,user.getUsername(),pageable);
            }
        }
        //高级查找
        else{
            //日志
            logger.info("---高级查询---");
            String author=request.getParameter("author");
            String infor=request.getParameter("infor");
            String model=request.getParameter("model");
            String title=request.getParameter("title");
            String firstDate1=request.getParameter("firstDate");
            String lastDate1=request.getParameter("lastDate");
            //字符串对象转为日期对象
            Date firstDate=null;
            Date lastDate=null;
            try {
                if(!StringUtil.isNull(firstDate1)){
                    firstDate=new SimpleDateFormat("yyyy-MM-dd").parse(firstDate1);
                }
                if(!StringUtil.isNull(lastDate1)) {
                    lastDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastDate1);
                }
            } catch (ParseException e) { //不以“-”格式输入日期则无法正确转换
                e.printStackTrace();
                map.put("draw", draw);
                map.put("result", 0);
                map.put("message", "日期格式错误！");
                return map;
            }
            //空搜(高级搜索如果空搜的话返回所有文档，包括不是本人编辑的，这不符合权限）
            if(StringUtil.isNull(author)&&StringUtil.isNull(infor)&&StringUtil.isNull(title)&&StringUtil.isNull(model)&&StringUtil.isNull(firstDate1)&&StringUtil.isNull(lastDate1)){
                //日志
                logger.info("无关键词搜索--默认按权限获取文档");
                documentPage=documentService.selectByRole(user,currentPage-1,size,documentRepository);
            }
            else {
                //日志
                logger.info("---高级查询---");
                UsefulDocument document = new UsefulDocument(user.getUsername(), title, model, infor,
                                                            firstDate, lastDate);
                documentPage = documentService.activeSearch(document, currentPage - 1, size,documentRepository);
            }
        }

        //剔除文档内容，传送轻便
        List<DocumentInfo> documentInfoList = documentService.documentsToDocumentInfos(documentPage.getContent());
        //总记录数
        long total = documentPage.getTotalElements();
        logger.info("total="+total);

        map.put("draw", draw);
        map.put("result", 1);
        if(0==total){
            map.put("message", "未查询到文档！");
        }else {
            map.put("total", total);
            map.put("pageData", documentInfoList);
            map.put("message", "成功获取分页数据！");
        }
        return map;
    }

    /**
     * 浏览文档
     **/
    @RequestMapping(value = "/getDocumentById",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getDocumById(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam("id")Integer id){
        Map<String,Object> map =new HashMap<String,Object>();

        logger.info("---浏览文档：id="+id+"---");

        Document document = documentRepository.findOne(id);
        if(document!=null){
            map.put("result", 1);
            map.put("message", "成功获取文档！");
            logger.info("成功获取文档！");
        }else{
            map.put("result", 0);
            map.put("message", "获取文档失败！");
            logger.error("获取文档失败！");
        }
        map.put("data",document);
        return map;
    }

    /**
     * 保存文档，若id不存在则新建文档
     **/
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> save(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map =new HashMap<String,Object>();

        String id = request.getParameter("id");
        String content = request.getParameter("content");
        String title = request.getParameter("title");
        String infor = request.getParameter("infor");
        String group = request.getParameter("group");

        if(StringUtil.isNull(content)||StringUtil.isNull(title)||
               StringUtil.isNull(group)){
            map.put("result", 0);
            map.put("message", "文档保存失败,信息不完整！");
            return map;
        }

        Users user=usersService.getCurrentUser();
        String userName=user.getUsername();
        int userId=user.getId();

        Document document;
        boolean flag = true;

        //更新文档
        if (id!=null&&!id.equals("")&&Integer.parseInt(id)>0){
            logger.info("更新文档：id="+id+"model="+group+"title="+title);
            document = documentRepository.findOne(Integer.parseInt(id));
            if(document==null){
                flag= false;
            }else {
                document.setModifyDate(new Date());
            }
        }
        //新建文档(所以前端新建文档时，传的id要为空，即使id被有意篡改也不会覆盖到原有数据)
        else{
            logger.info("新建文档：model="+group+"title="+title);
            document = new Document();
            document.setCreateDate(new Date());
            //新建时要加一个修改时间吗？
            document.setModifyDate(new Date());
        }

        if(flag){
            document.setContent(content);
            document.setTitle(title);
            document.setInfor(infor);
            document.setModel(group);
            document.setAuthor(userName);
            document.setAuthorId(userId);

            Document document1 = documentRepository.save(document);
            map.put("result", 1);
            map.put("data", document1.getId().intValue());
            map.put("message", "成功保存文档！");
            logger.info("成功保存文档！");
        }else{
            map.put("result", 0);
            map.put("data", 0);
            map.put("message", "文档更新失败,请确认文档是否存在！");
            logger.error("保存文档失败！");
        }
        map.put("data",document);
        return map;
    }

    /**
     * 删除文档
     **/
    @RequestMapping(value="delete",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> delete(@RequestParam("id")Integer id){
        Map<String,Object> map=new HashMap<>();

        logger.info("删除文档：id="+id);

        Document document=documentRepository.findOne(id);
        if (document == null) {
            map.put("result", 0);
            logger.info("删除文档失败");
            map.put("message", "删除文档失败！");
        }else{
            documentRepository.delete(id);
            logger.info("成功删除文档");
            map.put("result", 1);
            map.put("message", "成功删除文档！");
        }
        return map;
    }

    /**
     * 获取所有的文档标题和id等信息
     **/
    @RequestMapping(value="/getAll",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getAll(){
        Map<String,Object> map=new HashMap<>();
        logger.info("获取所有的文档标题和id等信息");

        List<Document> documentList=documentRepository.findAll();

        if (documentList == null||documentList.size()==0) {
            map.put("documentInfoList",null);
            map.put("result", 0);
            logger.info("不存在文档！");
            map.put("message", "不存在文档！");
        }else{
            //剔除文档内容，传送轻便
            List<DocumentInfo> documentInfoList = documentService.documentsToDocumentInfos(documentList);
            map.put("documentInfoList",documentInfoList);
            map.put("result", 1);
            map.put("message", "成功获取文档！");
        }
        return map;
    }
}