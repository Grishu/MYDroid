package com.demo.draganddropviewdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class DragAndDrop extends Activity
{
	private ImageView m_ivImage, m_ivImage1;
	private int m_counter = 0;
	float m_lastTouchX, m_lastTouchY, m_posX, m_posY, m_prevX, m_prevY, m_imgXB, m_imgYB, m_imgXC, m_imgYC, m_dx, m_dy;
	private LinearLayout m_llTop;
	private AbsoluteLayout m_alTop;
	private Button m_btnAddView, m_btnRemove;
	private Context m_context;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_drop_layout);
		
		m_context = this;
		
		m_prevX = 0;
		m_prevY = 0;
		m_imgXB = 50;
		m_imgYB = 100;
		m_imgXC = 150;
		m_imgYC = 100;
		
		m_ivImage = (ImageView) findViewById(R.id.ddivImage);
		m_ivImage1 = (ImageView) findViewById(R.id.ddivImage1);
		m_llTop = (LinearLayout) findViewById(R.id.ddllTop);
		m_alTop = (AbsoluteLayout) findViewById(R.id.ddalTop);
		m_btnAddView = (Button) findViewById(R.id.ddbtnAdd);
		m_btnRemove = (Button) findViewById(R.id.ddbtnRemove);
		
		m_ivImage.setOnTouchListener(m_onTouchListener);
		m_ivImage1.setOnTouchListener(m_onTouchListener);
		m_btnAddView.setOnClickListener(m_onClickListener);
		m_btnRemove.setOnClickListener(m_onClickListener);
		
	}
	
	/**
	 * Common click listener for clickable controls
	 */
	OnClickListener m_onClickListener = new OnClickListener(){
		
		@Override
		public void onClick(View p_v)
		{
			switch (p_v.getId())
				{
					case R.id.ddbtnAdd:
						addView();
						break;
					case R.id.ddbtnRemove:
						removeView();
						break;
					default:
						break;
				}
		}
	};
	
	/**
	 * Touch listener for view
	 */
	OnTouchListener m_onTouchListener = new OnTouchListener(){
		
		@Override
		public boolean onTouch(View p_v, MotionEvent p_event)
		{
			switch (p_event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
					{
						m_lastTouchX = p_event.getX();
						m_lastTouchY = p_event.getY();
						break;
					}
					case MotionEvent.ACTION_UP:
					{
						break;
					}
					
					case MotionEvent.ACTION_MOVE:
					{
						m_dx = p_event.getX() - m_lastTouchX;
						m_dy = p_event.getY() - m_lastTouchY;
						
						m_posX = m_prevX + m_dx;
						m_posY = m_prevY + m_dy;
						
						if (m_posX > 0 && m_posY > 0 && (m_posX + p_v.getWidth()) < m_alTop.getWidth() && (m_posY + p_v.getHeight()) < m_alTop.getHeight())
						{
							p_v.setLayoutParams(new AbsoluteLayout.LayoutParams(p_v.getMeasuredWidth(), p_v.getMeasuredHeight(), (int) m_posX, (int) m_posY));
							
							m_prevX = m_posX;
							m_prevY = m_posY;
							
						}
						
						break;
						
					}
					
				}
			return true;
		}
	};
	
	/**
	 * Add view dynamically for drag and drop
	 */
	private void addView()
	{
		ImageView m_img = new ImageView(m_context);
		TextView m_tv=new TextView(m_context);
		if (m_counter < 5)
		{
			if (m_counter % 2 == 0)
			{
				m_img.setBackgroundResource(R.drawable.bol_green);
				m_tv.setText("Hello! Drag Me! ");
				m_alTop.addView(m_tv, new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, ((int) m_imgXB), ((int) m_imgYB)));
				m_alTop.addView(m_img, new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, ((int) m_imgXB), ((int) m_imgYB)));
			}
			else
			{
				m_img.setBackgroundResource(R.drawable.bol_paars);
				m_alTop.addView(m_img, new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, ((int) m_imgXC), ((int) m_imgYC)));
			}
			m_counter++;
			if (m_counter == 5)
				m_btnAddView.setEnabled(false);
		}
		
		m_img.setOnTouchListener(m_onTouchListener);
		m_tv.setOnTouchListener(m_onTouchListener);
	}
	
	public void removeView()
	{
		m_counter = 0;
		m_alTop.removeAllViews();
		m_alTop.invalidate();
		m_btnAddView.setEnabled(true);
	}
	
	@Override
	public void onBackPressed()
	{
		this.clearView();
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy()
	{
		this.clearView();
		super.onDestroy();
	}
	
	/**
	 * Clear the views and free memory
	 */
	public void clearView()
	{
		if (m_context != null)
			m_context = null;
		
		if (m_llTop != null)
			m_llTop = null;
		
		if (m_alTop != null)
			m_alTop = null;
		
		if (m_btnAddView != null)
			m_btnAddView = null;
		
		if (m_btnRemove != null)
			m_btnRemove = null;
		
		if (m_ivImage != null)
			m_ivImage = null;
		
		if (m_ivImage1 != null)
			m_ivImage1 = null;
	}
	
}
