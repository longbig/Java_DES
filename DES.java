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
        //ָ����Կ����
        kg.init(168);
        SecretKey k=kg.generateKey();
        //������Կ���л�����
        FileOutputStream f=new FileOutputStream("key.dat");
        ObjectOutputStream b=new ObjectOutputStream(f);
        b.writeObject(k);
        //�ֽڷ�ʽ������Կ
        byte[ ] kb=k.getEncoded( );
        FileOutputStream f2=new FileOutputStream("keykb.dat");
        f2.write(kb);
        b.close();
        f.close();
        f2.close();
	}
	
	//��String content���м���
	public byte[] encrypt() throws Exception{
        FileInputStream f=new FileInputStream("key.dat");
        ObjectInputStream b=new ObjectInputStream(f);
        Key k=(Key)b.readObject();
        b.close();
        f.close();
        //����������ָ�����������������еĲ������������,���ǰ�������ѧ�㷨����
        Cipher cp=Cipher.getInstance("DESede");
        
        //��ʼ��Cipher����
        //ENCRYPT_MODE,��������
        //DECRYPT_MODE,��������
        //WRAP_MODE,��һ��Key��װ���ֽڣ������������а�ȫ����
        //UNWRAP_MODE,��ǰ���ѷ�װ����Կ�⿪��java.security.Key����
        cp.init(Cipher.ENCRYPT_MODE, k);
        //ָ���ַ����������ƽ̨����
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
        
        Cipher cp=Cipher.getInstance("DESede");// ����������
        cp.init(Cipher.DECRYPT_MODE, k);
        byte[]ptext=cp.doFinal(ctext);
        miwen=new String(ptext,"UTF8");
//        System.out.println(p);
		return ptext;
	}
}
