package com.app.myflowlayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
/**
 * 实现流式布局的自定义控件
 * @author dh
 *
 */
public class FlowLayout extends ViewGroup{

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public FlowLayout(Context context) {
		this(context,null);
	}
    
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int sizeWidth=MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth=MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight=MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight=MeasureSpec.getMode(heightMeasureSpec);
		//wrap content
		int width=0;
		int height=0;
		//行宽和行高
		int lineWidth=0;
		int lineHeight=0;
		//得到内部元素的个数
		int count=getChildCount();
		for(int i=0;i<count;i++){
			View child=getChildAt(i);
			//测量子控件的宽和高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams lp=(MarginLayoutParams) child.getLayoutParams();
			//子view占据的宽度
			int childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
			//子view占据的高度
			int childHeight=child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
			//换行
			if(lineWidth+childWidth>sizeWidth-getPaddingLeft()-getPaddingRight()){
				//对比得到最大宽度
				width=Math.max(width, lineWidth);
				//重置行宽
				lineWidth=childWidth;
				//记录行高
				height+=lineHeight;
				lineHeight=childHeight;
			}else{
				//叠加行宽
				lineWidth+=childWidth;
				//得到当前行最大的高度
				lineHeight=Math.max(lineHeight, childHeight);
			}
			//最后一个控件
			if(i==count-1){
				width=Math.max(lineWidth, width);
				height+=lineHeight;
			}
			
		}
		setMeasuredDimension(modeWidth==MeasureSpec.EXACTLY?sizeWidth:width+getPaddingLeft()+getPaddingRight(),
				modeHeight==MeasureSpec.EXACTLY?sizeHeight:height+getPaddingTop()+getPaddingBottom());
	}
	
	
	//存储所有的view
	private List<List<View>> mallviews=new ArrayList<List<View>>();
	//每一行的高度
	private List<Integer> mLineheight=new ArrayList<Integer>();
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mallviews.clear();
		mLineheight.clear();
		//当前viewgroup的宽度
		int width=getWidth();
		//
		int lineWidth=0;
		int lineHeight=0;
		List<View> lineviews=new ArrayList<View>();
		int count=getChildCount();
		for(int j=0;j<count;j++){
			View child=getChildAt(j);
			MarginLayoutParams lp=(MarginLayoutParams) child.getLayoutParams();
			int childWidth=child.getMeasuredWidth();
			int childHeight=child.getMeasuredHeight();
			//如果需要换行
			if(childWidth+lineWidth+lp.leftMargin+lp.rightMargin>width-getPaddingLeft()-getPaddingRight()){
				//记录当前view
				mallviews.add(lineviews);
				//记录行高
				mLineheight.add(lineHeight);
				//重置行宽和行高
				lineWidth=0;
				lineHeight=childHeight+lp.topMargin+lp.bottomMargin;
				//重置我们的view集合
				lineviews=new ArrayList<View>();
			}
			lineWidth+=childWidth+lp.leftMargin+lp.rightMargin;
			lineHeight=Math.max(lineHeight, childHeight+lp.topMargin+lp.bottomMargin);
			lineviews.add(child);
		}
		//处理最后一行
		mLineheight.add(lineHeight);
		mallviews.add(lineviews);
		
		//设置子view的位置
		int left=getPaddingLeft();
		int top=getPaddingTop();
		//行数
		int linenum=mallviews.size();
		for(int i=0;i<linenum;i++){
			//当前行的所有view
			lineviews=mallviews.get(i);
			//当前行的行高
			lineHeight=mLineheight.get(i);
			for(int j=0;j<lineviews.size();j++){
				View child=lineviews.get(j);
				if(child.getVisibility()==View.GONE){
					continue;
				}
				MarginLayoutParams lp=(MarginLayoutParams) child.getLayoutParams();
				int lc=left+lp.leftMargin;
				int tc=top+lp.topMargin;
				int rc=lc+child.getMeasuredWidth();
				int bc=tc+child.getMeasuredHeight();
				//为子view进行布局
				child.layout(lc, tc, rc, bc);
				left+=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
			}
			left=getPaddingLeft();
			top+=lineHeight;
		}
	}
   /*
    * 与当前viewgroup对应的layoutparams
    * @see android.view.ViewGroup#generateLayoutParams(android.util.AttributeSet)
    */
	 @Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		// TODO Auto-generated method stub
		return new MarginLayoutParams(getContext(), attrs);
	}
}
