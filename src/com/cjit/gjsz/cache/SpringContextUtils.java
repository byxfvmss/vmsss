package com.cjit.gjsz.cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext; // SpringӦ�������Ļ���

	/**
	 * ʵ��ApplicationContextAware�ӿڵĻص�������������Ļ���
	 * 
	 * @param applicationContext
	 * @throws BeansException
	 */
	public void setApplicationContext(ApplicationContext args)
			throws BeansException {
		applicationContext = args;
	}

	/**
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * ��ȡ����
	 * 
	 * @param name
	 * @return Object һ�����������ע���bean��ʵ��
	 * @throws BeansException
	 */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}

	/**
	 * ��ȡ����ΪrequiredType�Ķ���
	 * ���bean���ܱ�����ת������Ӧ���쳣���ᱻ�׳�BeanNotOfRequiredTypeException��
	 * 
	 * @param name
	 *            beanע����
	 * @param requiredType
	 *            ���ض�������
	 * @return Object ����requiredType���Ͷ���
	 * @throws BeansException
	 */
	public static Object getBean(String name, Class requiredType)
			throws BeansException {
		return applicationContext.getBean(name, requiredType);
	}

	/**
	 * ���BeanFactory��һ����������ƥ���bean���壬�򷵻�true
	 * 
	 * @param name
	 * @return boolean
	 */
	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	/**
	 * �ж��Ը�����ע���bean������һ��singleton����һ��prototype��
	 * ������������Ӧ��bean����û�б��ҵ��������׳�һ���쳣��NoSuchBeanDefinitionException��
	 * 
	 * @param name
	 * @return boolean
	 * @throws NoSuchBeanDefinitionException
	 */
	public static boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {
		return applicationContext.isSingleton(name);
	}

	/**
	 * @param name
	 * @return Class ע����������
	 * @throws NoSuchBeanDefinitionException
	 */
	public static Class getType(String name)
			throws NoSuchBeanDefinitionException {
		return applicationContext.getType(name);
	}

	/**
	 * �����bean������bean�������б����򷵻���Щ����
	 * 
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	public static String[] getAliases(String name)
			throws NoSuchBeanDefinitionException {
		return applicationContext.getAliases(name);
	}

}