class DNode {
    int key;
    int val;
    int freq;
    DNode next;
    DNode prev;
    DNode(int key, int val) {
        this.key = key;
        this.val = val;
        this.freq = 1;
    }
}

class DListNode {
    DNode front;
    DNode rear;
    int size = 0;

    DListNode() {
        this.size = 0;
        front = new DNode(-1, -1);
        rear = new DNode(-1, -1);
        front.next = rear;
        rear.prev = front;
    }

    public void addNode(DNode node) {
        DNode nextNode = front.next;
        node.next = nextNode;
        node.prev = front;

        front.next = node;
        nextNode.prev = node;

        size++;
    }

    public void removeNode(DNode node) {
        DNode nextNode = node.next;
        DNode prevNode = node.prev;

        nextNode.prev = prevNode;
        prevNode.next = nextNode;

        node.prev = node.next = null;

        size--;
    }
}

public class LFUCache {

    int size,count;
    Map<Integer, DNode> cache;
    Map<Integer, DListNode> freqMap;
    int minFrequency;
    public LFUCache(int capacity) {
        this.size = capacity;
        this.count = 0;
        this.cache = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFrequency = 0;
    }
    
    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1;
        }
        DNode node = cache.get(key);
        updateNode(node);
        return node.val;
    }
    
    public void put(int key, int value) {
        if (this.size == 0) {
            return;
        }

        if (!cache.containsKey(key)) {
            count++;

            if (count > size) {
                // remove
                DListNode nodeList = freqMap.get(minFrequency);
                DNode node = nodeList.rear.prev;
                nodeList.removeNode(node);
                cache.remove(node.key);
                count--;
            }
            minFrequency = 1;
            DNode node = new DNode(key, value);
            freqMap.putIfAbsent(minFrequency, new DListNode());
            DListNode nodeList = freqMap.get(minFrequency);
            nodeList.addNode(node);
            cache.put(key, node);
            return;
        }

        DNode node = cache.get(key);
        node.val = value;
        updateNode(node);
    }

    private void updateNode(DNode node) {
        DListNode listNode = freqMap.get(node.freq);
        listNode.removeNode(node);

        if (listNode.size == 0 && node.freq == minFrequency) {
            minFrequency++;
        }

        node.freq++;
        freqMap.putIfAbsent(node.freq, new DListNode());
        DListNode ref = freqMap.get(node.freq);
        ref.addNode(node);
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */