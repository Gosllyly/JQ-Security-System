package com.jqmk.examsystem.config;

import com.jqmk.examsystem.realm.RoleRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class ShiroConfig {

//    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager manager){
//        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
//        factoryBean.setSecurityManager(manager);
//
//        Map<String,String> map = new Hashtable<>();
//        map.put("/questionbank","authc");
//        map.put("/role","roles[admin]");
//
//
//        return factoryBean;
//    }
    @Value("${shiro.filterChainDefinitions}")
    private String filterChainDefinitions;

    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager manager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(manager);

        // 动态加载权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

//        // 动态设置用户权限，例如从数据库中读取
//        List<Role> roles = roleService.getAllRoles();
//        for (Role role : roles) {
//            String[] urls = role.getAuthUrls().split(",");
//            for (String url : urls) {
//                filterChainDefinitionMap.put(url, "perms[" + role.getRoleName() + "]");
//            }
//        }
        // 从配置文件加载过滤规则
        String[] definitions = filterChainDefinitions.split("[\\r\\n;]+"); // 支持换行符和分号分隔
        for (String definition : definitions) {
            String[] split = definition.split("=");
            if (split.length == 2) {
                filterChainDefinitionMap.put(split[0].trim(), split[1].trim());
            }
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("roleRealm") RoleRealm roleRealm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(roleRealm);
        return manager;
    }

    @Bean
    public RoleRealm roleRealm(){
        return new RoleRealm();
    }

}
