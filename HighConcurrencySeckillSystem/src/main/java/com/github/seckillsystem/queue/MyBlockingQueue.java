package com.github.seckillsystem.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列：JDK提供BlockQueue接口。提供了 7 个阻塞队列。
 * ArrayBlockingQueue ：一个由数组结构组成的有界阻塞队列。
 *LinkedBlockingQueue ：一个由链表结构组成的有界阻塞队列。 初始化时应指定其容量，否则默认为Integer..max。
 * 并发量大于ArrayBlockQueue
 * PriorityBlockingQueue ：一个支持优先级排序的无界阻塞队列。
 *DelayQueue：一个使用优先级队列实现的无界阻塞队列。
 *SynchronousQueue：一个不存储元素的阻塞队列。
 * LinkedTransferQueue：一个由链表结构组成的无界阻塞队列。
 * LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列
 * @param <E>
 */
public class MyBlockingQueue<E> {
    private List queue = new LinkedList();
    final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private int limit = 10;
    public MyBlockingQueue(){}
    private MyBlockingQueue(int limit){
        this.limit = limit;
    }
    public void put(E e) throws Exception{
        if(e == null)  throw new NullPointerException();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if(queue.size() == limit){
                notFull.await();
            }
                notEmpty.signal();
        }finally {
            lock.unlock();
        }
        queue.add(e);
    }
    public Object take() throws Exception{
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if(queue.size() == 0){
                notEmpty.await();
            }
                notFull.signal();
        }finally {
            lock.unlock();
        }
       return queue.remove(0);
    }
}
