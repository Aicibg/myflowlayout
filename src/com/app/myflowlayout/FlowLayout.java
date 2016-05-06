package com.app.myflowlayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
/**
 * ʵ����ʽ���ֵ��Զ���ؼ�
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
		//�п���и�
		int lineWidth=0;
		int lineHeight=0;
		//�õ��ڲ�Ԫ�صĸ���
		int count=getChildCount();
		for(int i=0;i<count;i++){
			View child=getChildAt(i);
			//�����ӿؼ��Ŀ�͸�
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams lp=(MarginLayoutParams) child.getLayoutParams();
			//��viewռ�ݵĿ��
			int childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
			//��viewռ�ݵĸ߶�
			int childHeight=child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
			//����
			if(lineWidth+childWidth>sizeWidth-getPaddingLeft()-getPaddingRight()){
				//�Աȵõ������
				width=Math.max(width, lineWidth);
				//�����п�
				lineWidth=childWidth;
				//��¼�и�
				height+=lineHeight;
				lineHeight=childHeight;
			}else{
				//�����п�
				lineWidth+=childWidth;
				//�õ���ǰ�����ĸ߶�
				lineHeight=Math.max(lineHeight, childHeight);
			}
			//���һ���ؼ�
			if(i==count-1){
				width=Math.max(lineWidth, width);
				height+=lineHeight;
			}
			
		}
		setMeasuredDimension(modeWidth==MeasureSpec.EXACTLY?sizeWidth:width+getPaddingLeft()+getPaddingRight(),
				modeHeight==MeasureSpec.EXACTLY?sizeHeight:height+getPaddingTop()+getPaddingBottom());
	}
	
	
	//�洢���е�view
	private List<List<View>> mallviews=new ArrayList<List<View>>();
	//ÿһ�еĸ߶�
	private List<Integer> mLineheight=new ArrayList<Integer>();
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mallviews.clear();
		mLineheight.clear();
		//��ǰviewgroup�Ŀ��
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
			//�����Ҫ����
			if(childWidth+lineWidth+lp.leftMargin+lp.rightMargin>width-getPaddingLeft()-getPaddingRight()){
				//��¼��ǰview
				mallviews.add(lineviews);
				//��¼�и�
				mLineheight.add(lineHeight);
				//�����п���и�
				lineWidth=0;
				lineHeight=childHeight+lp.topMargin+lp.bottomMargin;
				//�������ǵ�view����
				lineviews=new ArrayList<View>();
			}
			lineWidth+=childWidth+lp.leftMargin+lp.rightMargin;
			lineHeight=Math.max(lineHeight, childHeight+lp.topMargin+lp.bottomMargin);
			lineviews.add(child);
		}
		//�������һ��
		mLineheight.add(lineHeight);
		mallviews.add(lineviews);
		
		//������view��λ��
		int left=getPaddingLeft();
		int top=getPaddingTop();
		//����
		int linenum=mallviews.size();
		for(int i=0;i<linenum;i++){
			//��ǰ�е�����view
			lineviews=mallviews.get(i);
			//��ǰ�е��и�
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
				//Ϊ��view���в���
				child.layout(lc, tc, rc, bc);
				left+=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
			}
			left=getPaddingLeft();
			top+=lineHeight;
		}
	}
   /*
    * �뵱ǰviewgroup��Ӧ��layoutparams
    * @see android.view.ViewGroup#generateLayoutParams(android.util.AttributeSet)
    */
	 @Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		// TODO Auto-generated method stub
		return new MarginLayoutParams(getContext(), attrs);
	}
}
