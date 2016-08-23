package com.starsep.sokoban;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.starsep.sokoban.sokoban.Gameplay;
import com.starsep.sokoban.sokoban.Level;
import com.starsep.sokoban.sokoban.Position;
import com.starsep.sokoban.sokoban.Tile;

public class GameView extends View {
	private Rect dimension;
	private Gameplay gameplay;
	private int size; // size of tile
	private Paint textPaint;
	private AchievementListener achievementListener;
	private Position screenDelta;

	public GameView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		screenDelta = new Position(0, 0);
		Textures.init(getContext());
		int size = Math.min(getWidth(), getHeight()) / 10;
		dimension = new Rect(0, 0, size, size);
		gameplay = new Gameplay(this);
		setOnTouchListener(new OnSwipeTouchListener(getContext()) {
			public void onSwipeTop() {
				level().moveUp();
			}

			public void onSwipeRight() {
				level().moveRight();
			}

			public void onSwipeLeft() {
				level().moveLeft();
			}

			public void onSwipeBottom() {
				level().moveDown();
			}
		});
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
	}

	private Level level() {
		return gameplay.currentLevel();
	}

	private void setDrawingDimension(int x, int y) {
		dimension.set(screenDelta.x + x * size, screenDelta.y + y * size,
				screenDelta.x + (x + 1) * size, screenDelta.y + (y + 1) * size);
	}

	private void drawBackground(Canvas canvas) {
		for (int y = 0; y < level().height(); y++) {
			for (int x = 0; x < level().width(); x++) {
				setDrawingDimension(x, y);
				canvas.drawBitmap(Textures.tile(Tile.ground), null, dimension, null);
			}
		}
	}

	private void drawHero(Canvas canvas) {
		setDrawingDimension(level().player().x, level().player().y);
		canvas.drawBitmap(Textures.heroTexture(), null, dimension, null);
	}

	private void drawTiles(Canvas canvas) {
		for (int y = 0; y < level().height(); y++) {
			for (int x = 0; x < level().width(); x++) {
				setDrawingDimension(x, y);
				canvas.drawBitmap(level().texture(y, x), null, dimension, null);
			}
		}
	}

	private void drawTextOnRight(Canvas canvas, String text, int line) {
		final float textMargin = size / 2f;
		float movesWidth = textPaint.measureText(text);
		canvas.drawText(text, getWidth() - movesWidth - textMargin, line * size, textPaint);
	}

	private void drawStats(Canvas canvas) {
		drawTextOnRight(canvas, "Moves: " + gameplay.moves(), 1);
		drawTextOnRight(canvas, "Points: " + gameplay.points(), 2);
		drawTextOnRight(canvas, "Total Points: " + gameplay.totalPoints(), 3);
	}

	private void updateSize() {
		int newSize = Math.min(getWidth() / level().width(), getHeight() / level().height());
		if (newSize != size) {
			size = newSize;
			textPaint.setTextSize(size);
			screenDelta.x = ((getWidth() / size - level().width()) * size + getWidth() % size) / 2;
			screenDelta.y = ((getHeight() / size - level().height()) * size + getHeight() % size) / 2;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		updateSize();
		drawBackground(canvas);
		drawTiles(canvas);
		drawHero(canvas);
		drawStats(canvas);
		// achievementListener.onAchievementUnlock(R.string.achievement_leet);
	}

	public void setAchievementListener(AchievementListener achievementListener) {
		this.achievementListener = achievementListener;
	}

	public void update() {
		invalidate();
	}

	public void showWinDialog(int levelNumber, int points, int totalPoints) {
		new AlertDialog.Builder(getContext())
				.setTitle("Level " + levelNumber + " completed!")
				.setMessage("Points: " + points + "\n" + "Total: " + totalPoints + "\nNext level?")
				.setPositiveButton("Sure!", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						gameplay.nextLevel();
					}
				})
				.setNegativeButton("Repeat", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						gameplay.repeatLevel();
					}
				})
				.setIcon(android.R.drawable.ic_dialog_info)
				.setCancelable(false)
				.show();
	}
}
