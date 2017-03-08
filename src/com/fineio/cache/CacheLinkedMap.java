package com.fineio.cache;



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by daniel on 2017/3/7.
 */
public class CacheLinkedMap<T> {
    //链表只是控制释放不一定保存了所有的值
    private CacheObject<T> head;
    private CacheObject<T> foot;

    private Map<T, CacheObject<T>> indexMap = new ConcurrentHashMap<T, CacheObject<T>>();

    public boolean update(T t){
        synchronized (this){
            CacheObject<T> co = indexMap.get(t);
            if(co != null){
                co.updateTime();
                doChange(co);
                return true;
            }
        }
        return false;
    }

    public long getIdle(T t) {
        CacheObject<T> co = indexMap.get(t);
        if(co != null){
            return co.getIdle();
        }
        return 0;
    }

    public Iterator<T> iterator(){
        return indexMap.keySet().iterator();
    }

    private void doChange(CacheObject<T> co) {
        if(co.getLast() == null && co.getNext() == null){
            if(co != head) {
                co.setNext(head);
                head = co;
                if (foot == null) {
                    foot = co;
                }
            }
        } else if(head != co) {
            //此情况下不存在co.getLast为null的情况如果getLast是null 那么必然head == co
            co.getLast().setNext(co.getNext());
            if(co  == foot) {
                foot = co.getLast();
            }
            co.setNext(head);
            co.setLast(null);
            head = co;
        }
    }

    public void put(T t){
        synchronized (this){
            CacheObject<T> co = indexMap.get(t);
            if(co == null){
                co = new CacheObject<T>(t);
                indexMap.put(t, co);
            } else {
                co.updateTime();
            }
            doChange(co);
        }
    }


    public void remove(T t){
        synchronized (this){
            CacheObject<T> co = indexMap.get(t);
            if(co == null){
                return;
            }
            if(co.getNext() == null && co.getLast() == null ){
                //如果是head 那么该情况下foot也是 co
                if( head == co ){
                    head = null;
                    foot = null;
                }
            } else if(head == co) {
                //该情况getNext肯定非空
                head = co.getNext();
            } else {
                //head 不是 co的情况 last肯定非空
                co.getLast().setNext(co.getNext());
                if (co  == foot) {
                    foot = co.getLast();
                }
            }
            indexMap.remove(t);
        }
    }

    public  T peek() {
        synchronized (this) {
            return foot == null? null: foot.get();
        }
    }

    public T poll() {
        synchronized (this){
            if(foot != null){
                CacheObject<T> res = foot;
                if(foot.getLast() != null) {
                    foot.getLast().setNext(null);
                } else {
                    head = null;
                }
                foot = foot.getLast();
                res.setLast(null);
                res.setNext(null);
                return res.get();
            }
            return null;
        }
    }

}