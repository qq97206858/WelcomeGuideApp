package com.example.fqzhang.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.protobuftest.protobuf.PersonProbuf;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // 步骤1:通过 消息类的内部类Builder类 构造 消息类的消息构造器
        PersonProbuf.Person.Builder personBuilder = PersonProbuf.Person.newBuilder();
        JSONObject object = new JSONObject();
        try {
            object.put("name","Carson");
            object.put("id",123);
            object.put("email","carson.ho@foxmail.com");
            object.put("number","0157-23443276");
            // 把 person消息类对象 序列化为 byte[]字节数组
            System.out.println(Arrays.toString(object.toString().getBytes()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 步骤2:设置你想要设置的字段为你选择的值
        personBuilder.setName("Carson");// 在定义.proto文件时,该字段的字段修饰符是required,所以必须赋值
        personBuilder.setId(123);// 在定义.proto文件时,该字段的字段修饰符是required,所以必须赋值
        personBuilder.setEmail("carson.ho@foxmail.com"); // 在定义.proto文件时,该字段的字段修饰符是optional,所以可赋值 / 不赋值(不赋值时将使用默认值)

        PersonProbuf.Person.PhoneNumber.Builder phoneNumber =  PersonProbuf.Person.PhoneNumber.newBuilder();
        phoneNumber.setType( PersonProbuf.Person.PhoneType.HOME);// 直接采用枚举类型里的值进行赋值
        phoneNumber.setNumber("0157-23443276");
        // PhoneNumber消息是嵌套在Person消息里,可以理解为内部类
        // 所以创建对象时要通过外部类来创建

        // 步骤3:通过 消息构造器 创建 消息类 对象
        personBuilder.addPhone(phoneNumber);
        PersonProbuf.Person person = personBuilder.build();
        // 步骤4:序列化和反序列化消息(两种方式)

        /*方式1：直接 序列化 和 反序列化 消息 */
        // a.序列化
        byte[] byteArray1 = person.toByteArray();
        // 把 person消息类对象 序列化为 byte[]字节数组
        System.out.println(Arrays.toString(byteArray1));
        Log.d("zfq",Arrays.toString(byteArray1));
        // 查看序列化后的字节流

        // b.反序列化
        try {

            PersonProbuf.Person person_Request = PersonProbuf.Person.parseFrom(byteArray1);
            // 当接收到字节数组byte[] 反序列化为 person消息类对象
            System.out.println(person_Request.getName());
            System.out.println(person_Request.getId());
            System.out.println(person_Request.getEmail());
            System.out.println(person_Request.getPhone(0).getNumber());
            // 输出反序列化后的消息
        } catch (IOException e) {
            e.printStackTrace();
        }
       

        /*方式2：通过输入/ 输出流（如网络输出流） 序列化和反序列化消息 */
        // a.序列化
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {

            person.writeTo(output);
            // 将消息序列化 并写入 输出流(此处用 ByteArrayOutputStream 代替)

        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArray = output.toByteArray();
        // 通过 输出流 转化成二进制字节流

        // b. 反序列化
        ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
        // 通过 输入流 接收消息流(此处用 ByteArrayInputStream 代替)

        try {

            PersonProbuf.Person person_Request = PersonProbuf.Person.parseFrom(input);
            // 通过输入流 反序列化 消息

            System.out.println(person_Request.getName());
            System.out.println(person_Request.getId());
            System.out.println(person_Request.getEmail());
            // 输出消息
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
