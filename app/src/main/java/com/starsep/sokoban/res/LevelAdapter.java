package com.starsep.sokoban.res;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.starsep.sokoban.R;
import com.starsep.sokoban.Sokoban;
import com.starsep.sokoban.activity.GameActivity;
import com.starsep.sokoban.view.SquareButton;

public class LevelAdapter extends BaseAdapter {
	private final int size;
	private final Context context;

	public LevelAdapter(int size, Context context) {
		this.size = size;
		this.context = context;
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		Button result = new SquareButton(context);
		int levelNumber = i + 1;
		result.setOnClickListener(view1 -> {
			Intent intent = new Intent(context, GameActivity.class);
			intent.putExtra(Sokoban.NEW, true);
			intent.putExtra(Sokoban.LEVEL_NUMBER, levelNumber);
			context.startActivity(intent);
		});
		result.setText(String.format(context.getResources().getString(R.string.level), levelNumber));
		boolean levelFinished = false; // TODO: implement, get from database
		if (levelFinished) {
			result.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
		}
		return result;
	}
}
