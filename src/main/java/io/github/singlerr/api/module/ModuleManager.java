package io.github.singlerr.api.module;

import io.github.singlerr.utils.FileUtils;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

public final class ModuleManager {
    private static ModuleManager instance;

    private ModuleManager(){
        this.registeredModules = new HashMap<>();
    }
    private HashMap<String,Module> registeredModules;

    public static ModuleManager getInstance(){
        if(instance == null)
            return (instance = new ModuleManager());
        return instance;
    }

    public void loadModules(String modulePath){
        List<File> originFiles = FileUtils.getAllJarFiles(modulePath);
        for(File file : originFiles){
            try(JarFile jarFile = new JarFile(file)){
                ZipEntry confEntry = jarFile.getEntry("conf.xml");
                if(confEntry == null)
                    throw new IllegalArgumentException("A module must have conf.xml file in its jar file.");

                Document confXml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(jarFile.getInputStream(confEntry));
                Element root = confXml.getDocumentElement();

                NodeList mNodeList = root.getElementsByTagName("moduleName");
                if(mNodeList.getLength() > 1)
                    throw new IllegalArgumentException("A module config must have one module name node.");

                String moduleName =  mNodeList.item(0).getTextContent();

                NodeList pNodeList = root.getElementsByTagName("mainPath");
                if(pNodeList.getLength() > 1)
                    throw new IllegalArgumentException("A module config must have one main path node.");

                String mainPath = pNodeList.item(0).getTextContent();

                URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()},getClass().getClassLoader());
                Class<?> jarClass;
                try{
                    jarClass = loader.loadClass(mainPath);
                }catch (ClassNotFoundException ex){
                    throw new IllegalArgumentException(String.format("A class '%s' does not exist.",mainPath));
                }

                Class<? extends Module> moduleClass;
                try{
                    moduleClass = jarClass.asSubclass(Module.class);
                }catch (ClassCastException ex){
                    throw new IllegalArgumentException(String.format("A class '%s' does not extend Module class.",mainPath));
                }
                Module module = moduleClass.getConstructor().newInstance();
                module.init(moduleName);
                module.onEnable();
                registeredModules.put(moduleName,module);

                Logger.getGlobal().log(Level.INFO,String.format("Loaded %s.",moduleName));
            }catch (IOException | ParserConfigurationException | SAXException | NoSuchMethodException ex){
                ex.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Collection<Module> getRegisteredModules(){
        return registeredModules.values();
    }
    public Module getModule(String name){
        return registeredModules.get(name);
    }
}
