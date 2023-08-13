package com.geekplus.codegenerate;


//import org.junit.runner.RunWith;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;

/**
 * 单元测试继承该类即可
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//@Transactional
//@Rollback
//public abstract class Tester {}
//class ListNode{
//    int value;
//    ListNode next;
//    public ListNode(int value){
//        this.value=value;
//    }
//    public static void main(String args){
//        AddTwoListNode addTwoListNode=new AddTwoListNode();
//
//        System.out.println("value="+addTwoListNode.addTwoNumebr(new ListNode(6), new ListNode(2)););
//    }
//}
//class AddTwoListNode{
//    public ListNode addTwoNumebr(ListNode ln1,ListNode ln2){
//        ListNode first=new ListNode(0);
//        ListNode current = first;
//        int cal=0;
//        while(ln1!=null || ln2!=null){
//            int sum = cal;
//            if(ln1!=null){
//                sum+=ln1.value;
//                ln1=ln1.next;
//            }
//            if(ln2!=null){
//                sum+=ln2.value;
//                ln2=ln2.next;
//            }
//
//            current.next=new ListNode(sum%10);
//            current=current.next;
//            cal=sum/10;
//        }
//        if(cal>0){
//            current.next=new ListNode(cal);
//        }
//        return first.next;
//    }
//}
