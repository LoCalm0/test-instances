package indi.LoCalm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<TestClass> testClasses = Arrays.asList(
                TestClass.builder().id(1L).pid(-1L).sid(-1L).build(),
                TestClass.builder().id(2L).pid(1L).sid(-1L).build(),
                TestClass.builder().id(3L).pid(2L).sid(-1L).build(),
                TestClass.builder().id(4L).pid(3L).sid(-1L).build(),
                TestClass.builder().id(5L).pid(2L).sid(-1L).build(),

                TestClass.builder().id(6L).pid(5L).sid(-1L).build(),
                TestClass.builder().id(7L).pid(-1L).sid(-1L).build(),
                TestClass.builder().id(12L).pid(7L).sid(-1L).build(),
                TestClass.builder().id(142L).pid(7L).sid(-1L).build()

        );
        long sid = -1L;
        List<TestClass> tree = createTree(testClasses, sid);
        System.out.println(JSONUtil.toJsonStr(tree));
        long pid = 1L;
//        parentId
        Set<Long> childIds = getChildIds(testClasses, pid);
        System.out.println(childIds);

    }

    @Data
    @Builder
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    static class TestClass {
        @EqualsAndHashCode.Include
        private Long id;
        private Long pid;
        private Long sid;
        private List<TestClass> children;

        public void addChild(TestClass child) {
            if (this.children == null) {
                this.children = new ArrayList<>();
            }
            this.children.add(child);
        }
    }
    //帮我优化 createTree 方法

    /**
     * 根据id组装树
     *
     * @param items 数据
     * @param sid   id
     * @return 树
     */
    private static @NotNull List<TestClass> createTree(List<TestClass> items, Long sid) {
        if (CollUtil.isEmpty(items)) return Collections.emptyList();

        List<TestClass> roots = new ArrayList<>();
        Map<Long, TestClass> itemMap = items.stream().collect(Collectors.toMap(TestClass::getId, item -> item));

        for (TestClass item : items) {
            if (Objects.equals(item.getPid(), sid)) {
                roots.add(item);
            } else {
                TestClass parent = itemMap.get(item.getPid());
                if (parent != null) {
                    parent.addChild(item);
                }
            }
        }

        return roots;
    }


    /**
     * 根据id获取所有子级id和自己的id
     *
     * @param items    数据
     * @param parentId id
     * @return 所有子级id
     */
//    parentId  Long 改为  Set<Long> 怎么实现
    private static @NotNull Set<Long> getChildIds(List<TestClass> items, Long parentId) {
        if (CollUtil.isEmpty(items)) return Collections.emptySet();
        Map<Long, Set<Long>> childMap = items.stream().collect(Collectors.groupingBy(TestClass::getPid, Collectors.mapping(TestClass::getId, Collectors.toSet())));
        Set<Long> childIds = new HashSet<>();
        childIds.add(parentId);
        Queue<Long> queue = new LinkedList<>();
        queue.offer(parentId);

        while (!queue.isEmpty()) {
            Long currentId = queue.poll();
            if (childMap.containsKey(currentId)) {
                childIds.addAll(childMap.get(currentId));
                queue.addAll(childMap.get(currentId));
            }
        }

        return childIds;
    }

    private static @NotNull Set<Long> getChildIds(List<TestClass> items, Set<Long> parentIds) {
        if (CollUtil.isEmpty(items) || CollUtil.isEmpty(parentIds)) {
            return Collections.emptySet();
        }

        Map<Long, Set<Long>> childMap = items.stream().collect(Collectors.groupingBy(TestClass::getPid, Collectors.mapping(TestClass::getId, Collectors.toSet())));

        Queue<Long> queue = new LinkedList<>(parentIds);
        Set<Long> allChildIds = new HashSet<>(parentIds);

        while (!queue.isEmpty()) {
            Long currentId = queue.poll();
            if (childMap.containsKey(currentId)) {
                Set<Long> children = childMap.get(currentId);
                allChildIds.addAll(children);
                queue.addAll(children);
            }
        }

        return allChildIds;
    }

}
