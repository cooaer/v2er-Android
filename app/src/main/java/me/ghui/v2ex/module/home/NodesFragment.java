package me.ghui.v2ex.module.home;

import android.os.Bundle;

import me.ghui.v2ex.R;
import me.ghui.v2ex.module.base.BaseFragment;

/**
 * Created by ghui on 22/03/2017.
 */

public class NodesFragment extends BaseFragment {


	public static NodesFragment newInstance() {

		Bundle args = new Bundle();

		NodesFragment fragment = new NodesFragment();
		fragment.setArguments(args);
		return fragment;
	}


	@Override
	protected int attachLayoutId() {
		return R.layout.frag_simple_card;
	}

	@Override
	protected void initInjector() {

	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void fetchData() {

	}
}
