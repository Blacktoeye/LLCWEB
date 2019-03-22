package llcweb.com.component;

import llcweb.com.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by:Ricardo
 * Description: 可能废弃的类
 * Date: 2019/1/18
 * Time: 14:16
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private LoginServiceImpl userService;

    /**
     * 自定义验证方式
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        MyUserDetails user = (MyUserDetails) userService.loadUserByUsername(username);
        if(user == null){
            System.out.println("Username not found.");
            throw new BadCredentialsException("Username not found.");
        }

        //加密过程在这里体现
        System.out.println("user = "+username);
        System.out.println("passwd = "+user.getPassword());
        if (!password.equals(user.getPassword())) {
            System.out.println("Wrong password.");
            throw new BadCredentialsException("Wrong password.");
        }
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}