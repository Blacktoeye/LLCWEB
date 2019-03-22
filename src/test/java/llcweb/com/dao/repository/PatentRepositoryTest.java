package llcweb.com.dao.repository;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import llcweb.com.domain.models.Software;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import llcweb.com.domain.entity.UsefulPatent;
import llcweb.com.domain.models.Paper;
import llcweb.com.domain.models.Patent;
import llcweb.com.domain.models.Project;
import llcweb.com.service.PatentService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatentRepositoryTest {
	
	@Autowired
	private PatentRepository patentRepository;
	
	@Autowired
	private PatentService patentService;
	
	@Test
	public void add() throws ParseException {
		
		Patent patent = new Patent();
		
		for(int i = 1; i <= 8; i++) {
			patent.setId(i);
			patent.setTitle("网络组第" + i + "篇专利");
			patent.setAppliDate(new Date());
			patent.setIntroduction("aaaaa" + i);
			patent.setAuthorList("333" + i);
			patent.setOriginalLink("666" + i);
			patent.setBelongProject("555" + i);
			patent.setPublicDate(new Date());
			patent.setPublicNum("CN" + i);
			patent.setPublicDate(new Date(i));
			patent.setAgency("GDUT");
			patent.setAppliPeople("hahah");
			Assert.assertThat(patentRepository.save(patent), notNullValue());
		}
	}

		@Test
	    public void findAll() throws ParseException {
        UsefulPatent patent = new UsefulPatent();
	        patent.setFirstDate(new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01"));
	        patent.setLastDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-02"));
	        Page<Patent> patentList = patentService.findAll(patent,1,3);
	        Assert.assertThat(patentList.getTotalElements(),is(11L));
        //System.out.println(((Slice<Patent>) patent).getSort());
	    }
		
/*	    @Test
	    public void findByOneKey(){
	        Page<Patent> patentList = patentRepository.findByOneKey("test",new PageRequest(0,10, Sort.Direction.DESC,"publicDate"));
	        Assert.assertThat(patentList.getTotalElements(),is(12L));
	    }*/

	@Test
	public void getSoftwaresTest(){
		List<Patent> softwares=patentRepository.getLatest(4);
		Assert.assertThat(softwares.size(),greaterThan(1));
		for (Patent software:softwares){
			System.out.println(software.getTitle());
		}
	}

}
