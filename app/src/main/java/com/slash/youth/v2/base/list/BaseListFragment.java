package com.slash.youth.v2.base.list;

import com.core.op.lib.base.BFViewModel;
import com.core.op.lib.utils.inject.RootView;
import com.slash.youth.R;
import com.slash.youth.databinding.FrgBaselistBinding;
import com.slash.youth.v2.base.BaseFragment;

@RootView(R.layout.frg_baselist)
public abstract class BaseListFragment<V extends BFViewModel> extends BaseFragment<V, FrgBaselistBinding> {

}
