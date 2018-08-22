package com.bac.jedis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-redis.xml")
public class MyJedis {

    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    /*
    需求:操作String类型数据结构方法.
    方法:添加
     */
    @Test
    public void ValueOperationsSet() {
        //给String设置值.
        redisTemplate.boundValueOps("username").set("张无忌");
    }

    /*
 需求:操作String类型数据结构方法.
 方法:获取值
  */
    @Test
    public void ValueOperationsGet() {
        String username = (String) redisTemplate.boundValueOps("username").get();
        System.out.println(username);

    }

    /*
需求:操作String类型数据结构方法.
方法:删除
*/
    @Test
    public void dele() {
        //删除
        redisTemplate.delete("name");

    }

    /*
    需求:set集合操作
    方法:添加值
     */
    @Test
    public void SetOperationsSet() {
        redisTemplate.boundSetOps("itbac").add("猪八戒");
        redisTemplate.boundSetOps("itbac").add("孙悟空");
        redisTemplate.boundSetOps("itbac").add("沙和尚");
    }

    /*
  需求:set集合操作
  方法:获取值
   */
    @Test
    public void SetOperationsGet() {
        Set itbac = redisTemplate.boundSetOps("itbac").members();
        System.out.println(itbac);
    }

    /*
    需求:set集合操作
    方法:删除集合中的某个值
     */
    @Test
    public void removeOneValue() {
        redisTemplate.boundSetOps("itbac").remove("猪八戒");
    }

    /*
  需求:set集合操作
  方法:删除整个集合
   */
    @Test
    public void deleteAllValue() {
        redisTemplate.delete("namelist2");
    }

    /*
    需求:List集合操作
    方法:右压栈,后添加的对象排在右边
     */
    @Test
    public void ListOperationsRight() {
        redisTemplate.boundListOps("namelist1").rightPush("刘备");
        redisTemplate.boundListOps("namelist1").rightPush("关羽");
        redisTemplate.boundListOps("namelist1").rightPush("张飞");
    }

    ;

    /*
需求:List集合操作
方法:显示集合
 */
    @Test
    public void ShowRightList() {
        List range = redisTemplate.boundListOps("namelist1").range(0, 10);
        System.out.println(range);
    }

    ;

    /*
    需求:List集合操作
    方法:左压栈,后添加的对象排在左边
     */
    @Test
    public void ListOperationsLeft() {
        redisTemplate.boundListOps("namelist2").leftPush("刘备");
        redisTemplate.boundListOps("namelist2").leftPush("关羽");
        redisTemplate.boundListOps("namelist2").leftPush("张飞");
    }

    /*
  需求:List集合操作
  方法:根据索引查询集合某个元素
   */
    @Test
    public void testSearchByIndex() {
        Object namelist1 = redisTemplate.boundListOps("namelist1").index(0);
        System.out.println(namelist1);
    }
    /*
    需求:List集合操作
    方法:移除集合某个元素
     */
    @Test
    public void testRemoveByIndex(){
        redisTemplate.boundListOps("namelist1").remove(1,"关羽");
    }
    /*
    需求:操作Hash集合
    方法:存入值
     */
    @Test
    public void testSetValue(){
        redisTemplate.boundHashOps("namehash").put("a","唐僧");
        redisTemplate.boundHashOps("namehash").put("b","悟空");
        redisTemplate.boundHashOps("namehash").put("c","八戒");
        redisTemplate.boundHashOps("namehash").put("d","沙僧");
    }
    /*
  需求:操作Hash集合
  方法:提取所有的key
   */
    @Test
    public void testGetKeys(){
        Set namehash = redisTemplate.boundHashOps("namehash").keys();
        System.out.println(namehash);
    }
    /*
    需求:操作Hash集合
    方法:提取所有的值
    */
    @Test
    public void testGetValues(){
        List namehash = redisTemplate.boundHashOps("namehash").values();
        System.out.println(namehash);
    }
    /*
 需求:操作Hash集合
 方法:提取指定key的值
 */
    @Test
    public void testGetValuesByKey(){
        Object o = redisTemplate.boundHashOps("namehash").get("b");
        System.out.println(o);
    }
    /*
需求:操作Hash集合
方法:删除指定key的值
*/
    @Test
    public void testRemoveValuesByKey(){
        redisTemplate.boundHashOps("namehash").delete("c");
    }



}
