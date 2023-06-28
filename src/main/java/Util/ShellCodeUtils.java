//package Util;
//
//
//
//import java.io.IOException;
//import java.util.Properties;
//
///**
// * ShellCode加载器
// * 使用 com.sun.tools.attach 但是这个jar包是需要导入的
// * 首先tools.jar包是JDK自带的，pom.xml中依赖的包隐式依赖tools.jar包，而tools.jar并未在库中
// */
//public class ShellCodeUtils extends VirtualMachine {
//    protected ShellCodeUtils(AttachProvider attachProvider, String s) {
//        super(attachProvider, s);
//    }
//
//    public static void main(String[] args) {
////        com.sun.tools.attach.VirtualMachine
//    }
//
//    @Override
//    public void detach() throws IOException {
//
//    }
//
//    @Override
//    public void loadAgentLibrary(String s, String s1) throws AgentLoadException, AgentInitializationException, IOException {
//
//    }
//
//    @Override
//    public void loadAgentPath(String s, String s1) throws AgentLoadException, AgentInitializationException, IOException {
//
//    }
//
//    @Override
//    public void loadAgent(String s, String s1) throws AgentLoadException, AgentInitializationException, IOException {
//
//    }
//
//    @Override
//    public Properties getSystemProperties() throws IOException {
//        return null;
//    }
//
//    @Override
//    public Properties getAgentProperties() throws IOException {
//        return null;
//    }
//
//    @Override
//    public void startManagementAgent(Properties properties) throws IOException {
//
//    }
//
//    @Override
//    public String startLocalManagementAgent() throws IOException {
//        return null;
//    }
//}
