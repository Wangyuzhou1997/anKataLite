package cn.ankatalite.common.md.adapter;

/**
 * Created by per4j on 17/3/27.
 */

public interface IAdapterView<T> {
    void bind(T item, int position);
}
