package com.demo.draganddropviewdemo;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class DragAndDrop_old extends Activity
{
	
	private static String TAG = DragAndDrop_old.class.getSimpleName();
	private ImageView m_ivImage;
	
	private int NONE = 0, DRAG = 1, ZOOM = 2;
	private int m_Mode = NONE;
	private float m_oldDist = 1f;
	float mLastTouchX, mLastTouchY, mPosX, mPosY, prev_xa, prev_ya, prev_xb, prev_yb, prev_xt, prev_yt, dx, dy;
	private PointF m_midpoint = new PointF();
	private Matrix m_matrix = new Matrix(), m_savedMatrix = new Matrix();
	protected LayoutParams m_layoutParams;
	private int m_windowWidth, m_windowHeight;
	private LinearLayout m_llTop;
	private int offset_x = 0;
	private int offset_y = 0;
	private View selected_item = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_drop_layout);
		
		m_ivImage = (ImageView) findViewById(R.id.ddivImage);
		m_llTop = (LinearLayout) findViewById(R.id.ddllTop);
		
		m_windowWidth = getWindowManager().getDefaultDisplay().getWidth();
		m_windowHeight = getWindowManager().getDefaultDisplay().getHeight();
		
		// m_ivImage.setOnTouchListener(new OnTouchListener(){
		//
		// @Override
		// public boolean onTouch(View p_v, MotionEvent p_event)
		// {
		// drag(p_event, p_v);
		// return true;
		// }
		// });
		
		// m_ivImage.setOnTouchListener(new OnTouchListener(){
		//
		// @Override
		// public boolean onTouch(View p_v, MotionEvent p_event)
		// {
		// // RelativeLayout.LayoutParams m_params =
		// // (android.widget.RelativeLayout.LayoutParams)
		// // p_v.getLayoutParams();
		// m_layoutParams = (LayoutParams) m_ivImage.getLayoutParams();
		//
		// int X = (int) p_event.getX();
		// int Y = (int) p_event.getY();
		//
		// switch (p_event.getAction())
		// {
		// case MotionEvent.ACTION_DOWN:
		// int centerX = (int) (p_event.getX() + 25);
		// int centerY = (int) (p_event.getY() + 25);
		//
		//
		// break;
		// case MotionEvent.ACTION_MOVE:
		// int x_cord = (int) p_event.getRawX();
		// int y_cord = (int) p_event.getRawY();
		//
		// if (x_cord > m_windowWidth)
		// {
		// x_cord = m_windowWidth;
		// }
		// if (y_cord > m_windowHeight)
		// {
		// y_cord = m_windowHeight;
		// }
		//
		// m_layoutParams.leftMargin = x_cord - 25;
		// m_layoutParams.topMargin = y_cord - 75;
		//
		// m_ivImage.setLayoutParams(m_layoutParams);
		// break;
		// case MotionEvent.ACTION_UP:
		// // touch drop - just do things here after dropping
		//
		// break;
		// default:
		// break;
		// }
		//
		// return true;
		// }
		// });
		
		// m_ivImage.setOnTouchListener(m_onTouchListener);
		
		m_llTop.setOnTouchListener(new OnTouchListener(){
			
			@Override
			public boolean onTouch(View p_v, MotionEvent p_event)
			{
				switch (p_event.getActionMasked())
					{
						case MotionEvent.ACTION_MOVE:
							int x = (int) p_event.getX() - offset_x;
							int y = (int) p_event.getY() - offset_y;
							int w = getWindowManager().getDefaultDisplay().getWidth() - 100;
							int h = getWindowManager().getDefaultDisplay().getHeight() - 100;
							if (x > w)
								x = w;
							if (y > h)
								y = h;
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
							lp.setMargins(x, y, 0, 0);
							selected_item.setLayoutParams(lp);
							break;
						default:
							break;
					}
				return true;
			}
		});
		
		m_ivImage.setOnTouchListener(new OnTouchListener(){
			
			@Override
			public boolean onTouch(View p_v, MotionEvent p_event)
			{
				switch (p_event.getActionMasked())
					{
						case MotionEvent.ACTION_DOWN:
							offset_x = (int) p_event.getX();
							offset_y = (int) p_event.getY();
							selected_item = p_v;
							break;
						default:
							break;
					}
				
				return false;
			}
		});
		
	}
	
	public void drag(MotionEvent p_event, View v)
	{
		
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) v.getLayoutParams();
		
		switch (p_event.getAction())
			{
				case MotionEvent.ACTION_MOVE:
				{
					params.topMargin = (int) p_event.getRawY() - (v.getHeight());
					params.leftMargin = (int) p_event.getRawX() - (v.getWidth() / 2);
					v.setLayoutParams(params);
					break;
				}
				case MotionEvent.ACTION_UP:
				{
					params.topMargin = (int) p_event.getRawY() - (v.getHeight());
					params.leftMargin = (int) p_event.getRawX() - (v.getWidth() / 2);
					v.setLayoutParams(params);
					break;
				}
				case MotionEvent.ACTION_DOWN:
				{
					v.setLayoutParams(params);
					break;
				}
			}
	}
	
	OnTouchListener m_onTouchListener = new OnTouchListener(){
		
		@Override
		public boolean onTouch(View p_v, MotionEvent p_event)
		{
			final int action = p_event.getAction();
			m_layoutParams = (LayoutParams) m_ivImage.getLayoutParams();
			
			// switch (p_event.getAction())
			switch (action & MotionEvent.ACTION_MASK)
				{
				
					case MotionEvent.ACTION_DOWN:// first finger down
					{
						m_Mode = DRAG;
						
						mLastTouchX = p_event.getX();
						mLastTouchY = p_event.getY();
						
						break;
					}
					case MotionEvent.ACTION_UP:// first finger lifted
					{
						break;
					}
					
					case MotionEvent.ACTION_MOVE:
					{
						if (m_Mode == DRAG)
						{
							Log.d(TAG, "mode=DRAG");
							dx = p_event.getX() - mLastTouchX;
							dy = p_event.getY() - mLastTouchY;
							
							// Move the object
							if (p_v.equals(m_ivImage))
							{
								mPosX = prev_xa + dx;
								mPosY = prev_ya + dy;
								
							}
							
							if (mPosX > 0 && mPosY > 0 && (mPosX + p_v.getWidth()) > m_ivImage.getWidth() && (mPosY + p_v.getHeight()) > m_ivImage.getHeight())
							{
								// p_v.setLayoutParams(new
								// AbsoluteLayout.LayoutParams(p_v.getMeasuredWidth(),
								// p_v.getMeasuredHeight(), (int) mPosX, (int)
								// mPosY));
								// p_v.setLayoutParams(new
								// RelativeLayout.LayoutParams(p_v.getMeasuredWidth(),
								// p_v.getMeasuredHeight()));
								
								// m_layoutParams.width =
								// p_v.getMeasuredWidth();
								// m_layoutParams.height =
								// p_v.getMeasuredHeight();
								
								m_layoutParams.leftMargin = (int) mPosX;
								m_layoutParams.topMargin = (int) mPosY;
								m_layoutParams.rightMargin = (int) mPosX;
								m_layoutParams.bottomMargin = (int) mPosY;
								
								m_ivImage.setLayoutParams(m_layoutParams);
								
								if (p_v.equals(m_ivImage))
								{
									prev_xa = mPosX;
									prev_ya = mPosY;
								}
							}
							
						}
						break;
						
					}
					
				}
			return true;
		}
	};
	
	private void midPoint(PointF point, MotionEvent event)
	{
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
	
	private float spacing(MotionEvent event)
	{
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		Log.d("x ***", "" + x);
		Log.d("Y ***", "" + y);
		return FloatMath.sqrt(x * x + y * y);
	}
}
