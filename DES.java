import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * 
 */
public class DES {
	String mingwen;
	String miwen;
	public DES() throws Exception{init();}
	public void setMingwen(String content){
		this.mingwen=content;
	}
	public void init() throws Exception{
		KeyGenerator kg=KeyGenerator.getInstance("DESede");
        //指定密钥长度
        kg.init(168);
        SecretKey k=kg.generateKey();
        //对象密钥序列化保存
        FileOutputStream f=new FileOutputStream("key.dat");
        ObjectOutputStream b=new ObjectOutputStream(f);
        b.writeObject(k);
        //字节方式保存密钥
        byte[ ] kb=k.getEncoded( );
        FileOutputStream f2=new FileOutputStream("keykb.dat");
        f2.write(kb);
        b.close();
        f.close();
        f2.close();
	}
	
	//对String content进行加密
	public byte[] encrypt() throws Exception{
        FileInputStream f=new FileInputStream("key.dat");
        ObjectInputStream b=new ObjectInputStream(f);
        Key k=(Key)b.readObject();
        b.close();
        f.close();
        //它描述了由指定输入产生输出所进行的操作或操作集合,总是包含密码学算法名称
        Cipher cp=Cipher.getInstance("DESede");
        
        //初始化Cipher对象
        //ENCRYPT_MODE,加密数据
        //DECRYPT_MODE,解密数据
        //WRAP_MODE,将一个Key封装成字节，可以用来进行安全传输
        //UNWRAP_MODE,将前述已封装的密钥解开成java.security.Key对象
        cp.init(Cipher.ENCRYPT_MODE, k);
        //指定字符集，避免跨平台错误
        byte ptext[]=mingwen.getBytes("UTF8");
//        System.out.println("");
//        for(int i=0;i<ptext.length;i++){
//        	System.out.print(ptext[i]+", ");
//        }
//        System.out.println("");
        
        byte ctext[]=cp.doFinal(ptext);
        
        for(int i=0;i<ctext.length;i++){
        	System.out.print(ctext[i]+", ");
        }
        FileOutputStream f2=new FileOutputStream("SEnc.dat");
        f2.write(ctext);
        f2.close();
		return ctext;
	}
	
	public byte[] Decrypt(byte[] content)throws Exception{
		FileInputStream f=new FileInputStream("SEnc.dat");
        int num=f.available();
        byte[] ctext=new byte[num];
        f.read(ctext);
        f.close();
        // read the Decrypt from the previous file named SEnc.dat
//        for(int i=0;i<ctext.length;i++){
//        	System.out.print(ctext[i]+", ");
//        }  
//        System.out.println();
        
        FileInputStream f2=new FileInputStream("keykb.dat");
        int num2=f2.available();
        byte[] keykb=new byte[num2];
        f2.read(keykb);
        f2.close();
        SecretKeySpec k=new SecretKeySpec(keykb,"DESede");
        
        Cipher cp=Cipher.getInstance("DESede");// 创建密码器
        cp.init(Cipher.DECRYPT_MODE, k);
        byte[]ptext=cp.doFinal(ctext);
        miwen=new String(ptext,"UTF8");
//        System.out.println(p);
		return ptext;
	}
}
