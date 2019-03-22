package llcweb.com.service.impl;

import llcweb.com.dao.repository.DocumentRepository;
import llcweb.com.domain.models.Document;
import llcweb.com.service.DocumentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by:Ricardo
 * Description:
 * Date: 2019/1/12
 * Time: 20:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FileServiceImplTest {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentService documentService;

    @Test
    public void add(){
        Document document=new Document();
        document.setId(1);
        document.setAuthor("haien");
        document.setContent("balabala");
        document.setTitle("项目组第13次会议");
        document.setCreateDate(new Date());
        Assert.assertThat(documentRepository.save(document).getAuthor(),is("haien"));
    }

}