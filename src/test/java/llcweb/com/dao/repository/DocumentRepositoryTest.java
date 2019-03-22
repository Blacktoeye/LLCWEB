package llcweb.com.dao.repository;

import llcweb.com.domain.User;
import llcweb.com.domain.models.Document;
import llcweb.com.domain.models.Users;
import llcweb.com.service.DocumentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * @Author haien
 * @Description TODO
 * @Date 19:03 2018/8/22
 * @Param
 * @return
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentService documentService;

    @Autowired
    private UsersRepository usersRepository;

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

    @Test
    public void fuzzySearch(){
        Page<Document> documents=documentRepository.fuzzySearch("haien","chen",new PageRequest(0,10, Sort.Direction.DESC,"createDate"));
        Assert.assertThat(documents.getTotalElements(),is(12L));
    }

    @Test
    public void findByAuthorId(){
        Page<Document> documents=documentRepository.findByAuthorId(1,new PageRequest(0,10, Sort.Direction.DESC,"createDate"));
        System.out.println(documents.getTotalElements());
        Assert.assertThat(documents.getTotalElements(),is(3L));
    }


    @Test
    public void selectByRole(){
        Users users = usersRepository.findByUsername("chen");
        Page<Document> documents=documentService.selectByRole(users,0,10,documentRepository);
        System.out.println(documents.getTotalElements());
        Assert.assertThat(documents.getTotalElements(),greaterThan(3L));
    }

}