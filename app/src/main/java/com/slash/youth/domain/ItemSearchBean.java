package com.slash.youth.domain;
/**
 * Created by ZSS on 2016/9/19.
 */

public class ItemSearchBean {
 private String item;
 private boolean isShowRemoveBtn;

 public ItemSearchBean(String item, boolean isShowRemoveBtn) {
  this.item = item;
  this.isShowRemoveBtn = isShowRemoveBtn;
 }

 public String getItem() {
  return item;
 }

 public void setItem(String item) {
  this.item = item;
 }

 public boolean isShowRemoveBtn() {
  return isShowRemoveBtn;
 }

 public void setShowRemoveBtn(boolean showRemoveBtn) {
  isShowRemoveBtn = showRemoveBtn;
 }
}
