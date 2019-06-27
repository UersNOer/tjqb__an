package com.market.ssvip.white.component.adapter;

import android.support.annotation.Nullable;
import com.market.ssvip.white.utils.CheckUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created by st0rm23 on 2016/8/13. Reconstitute by LiCola on 2018/8/27 类型维护者，用于RecyclerView的列表多类型维护
 * 1：保持添加时顺序 2：外部获取时有序的得到每段的修正实际位置
 *
 * 利用ArrayList的插入有序特点和访问性能快速，HashMap的查处性能快速，共同维护多类型分区数据
 */
public class TypeMaintainer {


  static final String DEFAULT_GROUP_ID = "group-id";

  public static TypeMaintainer create() {
    return new TypeMaintainer();
  }

  //维护插入顺序
  private ArrayList<TypeSection> mTypeSections;
  //维护查找效率 通过type和groupId关联TypeSection
  private HashMap<String, TypeSection> mGroup2Sections;

  private int total_length;

  private TypeMaintainer() {
    this.mTypeSections = new ArrayList<>();
    this.mGroup2Sections = new HashMap<>();
    this.total_length = 0;
  }

  /**
   * 重置数据，但是依然维护数据位置关系
   */
  public void reset() {
    for (TypeSection section : mTypeSections) {
      section.clear();
    }
    total_length = 0;
  }

  /**
   * 固定某些类型集合的位置关系
   */
  public void fix(int... types) {
    if (CheckUtils.isEmpty(types)) {
      return;
    }

    for (int type : types) {
      fix(new TypeSection(type, DEFAULT_GROUP_ID));
    }
  }

  public void fix(int type, String groupId) {
    fix(new TypeSection(type, groupId));
  }

  private void fix(TypeSection typeSection) {
    mTypeSections.add(typeSection);
    mGroup2Sections.put(makeKey(typeSection.type, typeSection.groupId), typeSection);
  }


  public <T> void append(int type, List<T> listData) {
    notify(type, DEFAULT_GROUP_ID, listData, false, false);
  }

  public <T> void append(int type, T data) {
    notify(type, DEFAULT_GROUP_ID, Collections.singletonList(data), false, false);
  }

  public <T> void append(int type, String groupId, T data) {
    notify(type, groupId, Collections.singletonList(data), false, false);
  }

  public <T> void append(int type, String groupId, List<T> data) {
    notify(type, groupId, data, false, false);
  }

  public <T> void appendSingle(int type, List<T> data) {
    notify(type, DEFAULT_GROUP_ID, Collections.singletonList(data), false, false);
  }

  public <T> void appendSingle(int type, String groupId, List<T> data) {
    notify(type, groupId, Collections.singletonList(data), false, false);
  }

  public <T> void insertFirst(int type, T data) {
    notify(type, DEFAULT_GROUP_ID, Collections.singletonList(data), true, false);
  }

  public <T> void insertFirst(int type, List<T> data) {
    notify(type, DEFAULT_GROUP_ID, data, true, false);
  }

  public <T> void insertFirst(int type, String groupId, T data) {
    notify(type, groupId, Collections.singletonList(data), true, false);
  }

  public <T> void insertFirst(int type, String groupId, List<T> dataList) {
    notify(type, groupId, dataList, true, false);
  }

  public <T> boolean replace(int type, List<T> listData) {
    return notify(type, DEFAULT_GROUP_ID, listData, false, true);
  }

  public <T> boolean replace(int type, T data) {
    return notify(type, DEFAULT_GROUP_ID, Collections.singletonList(data), false, true);
  }

  public <T> boolean replace(int type, String groupId, T data) {
    return notify(type, groupId, Collections.singletonList(data), false, true);
  }

  public <T> boolean replace(int type, String groupId, List<T> data) {
    return notify(type, groupId, data, false, true);
  }

  private <T> boolean notify(int type, String groupId, List<T> dataList,
      boolean insertFirst, boolean replace) {
    //构造key
    String key = makeKey(type, groupId);
    //hash访问 取出分区
    TypeSection typeSection = mGroup2Sections.get(key);

    int oldSectionLength = 0;

    if (replace && typeSection != null) {
      //分区已经存在 且需要替换
      oldSectionLength = typeSection.length;
      typeSection.clear();
    }

    if (typeSection == null) {
      //分区不存在
      typeSection = new TypeSection(type, groupId);
      mGroup2Sections.put(key, typeSection);
      if (insertFirst) {
        //如果需要 插入头部
        mTypeSections.add(0, typeSection);
      } else {
        //否则 添加在末尾
        mTypeSections.add(typeSection);
      }
    }

    if (insertFirst) {
      typeSection.insert(dataList);
    } else {
      typeSection.add(dataList);
    }
    total_length += dataList.size() - oldSectionLength;
    return oldSectionLength > 0;
  }


  public boolean clear(int type) {
    return clear(type, DEFAULT_GROUP_ID);
  }

  /**
   * 清除某个类型 注意：不是删除remove，只清除掉该分区内的数据，并不会改变该分区的位置
   */
  public boolean clear(int type, String groupId) {
    String key = makeKey(type, groupId);
    TypeSection typeSection = mGroup2Sections.get(key);
    if (typeSection != null) {
      this.total_length -= typeSection.length;
      typeSection.clear();
      return true;
    }
    return false;
  }


  public int getType(int position) {
    int offset = 0;
    //遍历有序 数组
    for (TypeSection typeSection : mTypeSections) {
      int length = typeSection.length;
      if (position < offset + length) {
        //position 落在该分区段内 返回分区类型
        return typeSection.type;
      } else {
        //否则 累加前段分区长度
        offset += length;
      }
    }
    throw new IndexOutOfBoundsException("数组越界");
  }

  /**
   * 返回全局的position在type分区上的的位置
   */
  public int getTypePosition(int position) {
    int offset = 0;
    for (TypeSection typeSection : mTypeSections) {
      int length = typeSection.length;
      if (position < offset + length) {
        //position 落在该分区段内 修正偏移量 返回当前position的数据
        return position - offset;
      } else {
        offset += length;
      }
    }
    throw new IndexOutOfBoundsException("数组越界");
  }

  public <T> List<T> getTypeListData(int type) {
    return getTypeListData(type, DEFAULT_GROUP_ID);
  }

  public <T> List<T> getTypeListData(int type, String groupId) {
    String key = makeKey(type, groupId);
    TypeSection typeSection = mGroup2Sections.get(key);
    if (typeSection != null) {
      return typeSection.listTypeData;
    }
    return Collections.emptyList();
  }

  public <T> T getTypeDataByTypePosition(int type, int typePosition) {
    return getTypeDataByTypePosition(type, DEFAULT_GROUP_ID, typePosition);
  }


  public <T> T getTypeDataByTypePosition(int type, String groupId, int typePosition) {
    List<T> typeListData = getTypeListData(type, groupId);
    if (typeListData.isEmpty()) {
      return null;
    }
    return typeListData.get(typePosition);
  }

  public <T> T getTypeData(int position) {
    int offset = 0;
    for (TypeSection typeSection : mTypeSections) {
      int length = typeSection.length;
      if (position < offset + length) {
        //position 落在该分区段内 修正偏移量 返回当前position的数据
        return (T) typeSection.getListTypeData(position - offset);
      } else {
        offset += length;
      }
    }
    throw new IndexOutOfBoundsException("数组越界");
  }

  @Nullable
  public String getGroupId(int position) {
    int offset = 0;
    for (TypeSection typeSection : mTypeSections) {
      int length = typeSection.length;
      if (position < offset + length) {
        return typeSection.groupId;
      } else {
        offset += length;
      }
    }
    throw new IndexOutOfBoundsException("数组越界");
  }


  public int getFirstPositionByType(int type) {
    return getFirstPositionByType(type, DEFAULT_GROUP_ID);
  }


  public int getFirstPositionByType(int type, String groupId) {
    int offset = 0;
    for (TypeSection typeSection : mTypeSections) {
      if (typeSection.type == type && typeSection.groupId.equals(groupId)) {
        return offset;
      }
      offset += typeSection.length;
    }
    return -1;
  }

  public int getLastPositionByType(int type) {
    return getLastPositionByType(type, DEFAULT_GROUP_ID);
  }


  /**
   * 得到某个类型的 在列表上的实际最后位置
   */
  public int getLastPositionByType(int type, String groupId) {
    int offset = 0;
    for (TypeSection typeSection : mTypeSections) {
      offset += typeSection.length;
      if (typeSection.type == type && typeSection.groupId.equals(groupId)) {
        return offset - 1;
      }
    }
    return -1;
  }

  public int getTypeLength(int type) {
    return getTypeLength(type, DEFAULT_GROUP_ID);
  }


  public int getTypeLength(int type, String groupId) {
    String key = makeKey(type, groupId);
    TypeSection typeSection = mGroup2Sections.get(key);
    if (typeSection != null) {
      return typeSection.length;
    }
    return 0;
  }

  public int getCount() {
    return total_length;
  }

  public TypeMaintainer snapshoot() {
    TypeMaintainer snapshoot = new TypeMaintainer();
    for (TypeSection typeSection : mTypeSections) {
      snapshoot.mTypeSections.add(typeSection.snapshoot());
    }

    for (Entry<String, TypeSection> entry : mGroup2Sections.entrySet()) {
      snapshoot.mGroup2Sections.put(entry.getKey(), entry.getValue().snapshoot());
    }

    snapshoot.total_length = this.total_length;
    return snapshoot;
  }

  private static final String makeKey(int type, String groupId) {
    return type + "-/" + groupId;
  }

  private final static class TypeSection<T> {

    final int type;
    final String groupId;//分段id
    List<T> listTypeData;
    int length;

    TypeSection(int type, String groupId) {
      this.type = type;
      this.groupId = groupId;
      this.listTypeData = new ArrayList<>();
      this.length = 0;
    }

    void add(List<T> dataList) {
      listTypeData.addAll(dataList);
      length += dataList.size();
    }

    void insert(List<T> dataList) {
      listTypeData.addAll(0, dataList);
      length += dataList.size();
    }


    T getListTypeData(int offsetPosition) {
      return listTypeData.get(offsetPosition);
    }

    void clear() {
      length = 0;
      listTypeData.clear();
    }

    TypeSection<T> snapshoot() {
      TypeSection<T> shapshoot = new TypeSection<>(this.type, this.groupId);
      shapshoot.listTypeData.addAll(this.listTypeData);
      shapshoot.length = this.length;
      return shapshoot;
    }

  }

}
