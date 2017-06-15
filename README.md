"# drpc-proxy-copy" 
DRPC-Proxy�ǻ���ʹ��storm DRPC��RPC���񣬽���ҵ�������storm��ܴ����һ���򵥿�ܣ�
��ĳЩ�����£���ʹ��DRPC����ע��ʹ��storm����ʽ���������ͨ�������ʹ��DRPCServer��Ϊ�����ṩ����������bolt�д���ҵ��ReturnResults���ؽ����bolt�лὫҵ�������storm���뽻֯����ϣ�Ϊ������������չ�������⡣
DRPC-Proxy�ṩ����ҵ����storm���������ѷ�ʹ�ö�̬����������DRPCClient��DRPCServerͨѶ��DRPCServer������ƥ�䵽��Ӧ�ķ����ṩ�������ս����DRPCServer���ظ����ѷ���


### DRPC-Proxy �ص�
* ����storm��ҵ����룬���������ж�storm�޸�֪
* ʹ�ü򵥣�����jar����properties�������ط�������ã�pom.xml�����������profile
* ֧������ģʽ����������storm����ҵ�񿪷�-relyģʽ��LocalDRPCģʽ��Remoteģʽ
* �ṩspring�����µ�֧�֣���spring���
* �쳣��Զ���׳�
* ��DRPC�޷�װ��ʹ��ԭ���������
* ����AKKA����֤���߳���bolt�Ը߲�����֧��

### Module ˵��

proxy : ���ڽӿڵ�drpc����

proxy-spring : ֧��spring�����Ļ��ڽӿڵ�drpc����

demo��

demo-customer : ����������

demo-customer-spring : spring��������������

demo-server : ����ӿ�

demo-serviceimpl : �����ṩ��

demo-serviceimpl-spring : spring���������ṩ��

### �÷�1(��spring����)

### ����ӿ�API
```
 public interface UserService {
    User getUser(String name) throws MyException;
}
```
### �����ṩ��
```
public class UserServiceImpl implements UserService {
    public User getUser(String name) throws MyException{
        User user = new User();
        if("tom".equals(name)){
            user.setAge(12);
            user.setId(111L);
            user.setName("tom");
        }else {
            throw new MyOnlyException("ҵ���쳣");
        }
        return user;
    }
```
#### drpcproxy-provider.properties
```
service.impls=\
#  com.zrk1000.demo.serviceimpl.TestServiceImpl,\
  com.zrk1000.demo.serviceimpl.UserServiceImpl
drpc.spout.num=1
drpc.dispatch.bolt.num=1
drpc.result.bolt.num=1
drpc.spout.name=spout_name
drpc.topology.name=topology_name
```
#### �����ű�
```
storm jar provider.jar  com.zrk1000.proxy.ConfigMain drpcSpoutName topologyName
```
### ����������
```
public class Runner {
  public static void main(String[] args) {
      ServiceImplFactory.init();
      UserService userService = ServiceImplFactory.newInstance(UserService.class);
      User user = userService.getUser("tom");
      System.out.println("------------user:"+user.toString());
  }
}
```
#### drpcproxy-consumer.properties
```
drpc.client.config.storm.thrift.transport=org.apache.storm.security.auth.SimpleTransportPlugin
drpc.client.config.storm.nimbus.retry.times=3
drpc.client.config.storm.nimbus.retry.interval.millis=10000
drpc.client.config.storm.nimbus.retry.intervalceiling.millis=60000
drpc.client.config.drpc.max_buffer_size=104857600
drpc.client.host=192.168.1.81
drpc.client.port=3772
drpc.client.timeout=50000

topology.mapping.config.zrk1000-service-provider=\
#    com.zrk1000.demo.service.TestService,\
    com.zrk1000.demo.service.UserService

#topology.mapping.config.zrk1000-service-provider-spring=\
#    com.zrk1000.demo.service.UserService

#remote,local,rely
drpc.pattern=${profiles.pattern}


```

### �÷�2(spring����-springboot)

### ����ӿ�API
```
 public interface UserService {
    UserDto getUser(Long id);
}
```
### �����ṩ��
```
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    public UserDto getUser(Long id) {
        return convert(userRepository.findOne(id));
    }
   
}
```
#### drpcproxy-provider.properties
```
#ҵ�����ڵİ�����ʹ��AnnotationConfigApplicationContext����spring�����Ļ��� ������ʹ��springboot����֧�ֻ���xml����������
service.impls=com.zrk1000.demo
drpc.spout.num=1
drpc.dispatch.bolt.num=1
drpc.result.bolt.num=1
drpc.spout.name=spout_name
drpc.topology.name=topology_name


```
#### �����ű�
```
storm jar provider.jar  com.zrk1000.proxy.SpringMain drpcSpoutName topologyName
```
### ����������
```
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public UserDto getUser(@PathVariable("id") Long id){
        return userService.getUser(id);
    }
}
```
#### drpcproxy-consumer.properties
```
drpc.client.config.storm.thrift.transport=org.apache.storm.security.auth.SimpleTransportPlugin
drpc.client.config.storm.nimbus.retry.times=3
drpc.client.config.storm.nimbus.retry.interval.millis=10000
drpc.client.config.storm.nimbus.retry.intervalceiling.millis=60000
drpc.client.config.drpc.max_buffer_size=104857600
drpc.client.host=192.168.1.81
drpc.client.port=3772
drpc.client.timeout=5000

topology.mapping.config.zrk1000-service-provider-spring=\
#  com.zrk1000.demo.service.GroupService,\
  com.zrk1000.demo.service.UserService

```
#### StormConfig
```
@Profile({"local","remote"})
@Configuration
@ServiceScan(basePackages = "com.zrk1000.demo.service",
//        excludeClasses = {UserService.class},
        rpcHandleBeanRef="stormDrpcHandle")
public class StormConfig {

    @Profile("local")
    @Scope("singleton")
    @Bean("stormDrpcHandle")
    public RpcHandle getStormLocalRpcHandle(){
        StormLocalDrpcHandle drpcHandle = null;
        try {
            Set<String> serviceImpls = ServiceImplFactory.loadServiceImpls();
            SpringBoltHandle springBoltHandle = new SpringBoltHandle(serviceImpls.toArray(new String[serviceImpls.size()]));
            drpcHandle = new StormLocalDrpcHandle(springBoltHandle);
        } catch (IOException e) {
            throw new RuntimeException("��ʼ��stormDrpcHandleʧ��");
        }
        return  drpcHandle;
    }
    
    @Bean
    @ConfigurationProperties("drpc.client")
    public DrpcClientConfig getDrpcClientConfig(){
        return new DrpcClientConfig();
    }
    
    @Bean
    @ConfigurationProperties("topology.mapping")
    public TopologyMapping getTopologyMapping(){
        return new TopologyMapping();
    }

    @Profile("remote")
    @Bean("stormDrpcHandle")
    public RpcHandle getStormRemoteRpcHandle(DrpcClientConfig clientConfig,TopologyMapping topologyMapping){
        Config config = new Config();
        config.putAll(clientConfig.getConfig());
        return  new StormRemoteDrpcHandle(config,clientConfig.getHost(),clientConfig.getPort(),clientConfig.getTimeout(),topologyMapping.getConfig());
    }
    
    class DrpcClientConfig{
        private String host;
        private Integer port;
        private Integer timeout;
        private Map<String,String> config ;
        //getter setter
    }

    class TopologyMapping {
        Map<String, Set<String>> config;
        //getter setter
    }

}
```



