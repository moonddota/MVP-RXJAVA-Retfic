package app.com.skylinservice.manager.customeview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;


/**
 * Created by jameson on 8/30/16.
 */
public class CardScaleHelper {
    private RecyclerView mRecyclerView;
    private Context mContext;

    private float mScale = 0.9f; // 两边视图scale
    private int mPagePadding = 10; // 卡片的padding, 卡片间的距离等于2倍的mPagePadding
    private int mShowLeftCardWidth = 20;   // 左边卡片显示大小

    private int mCardWidth; // 卡片宽度
    private int mOnePageWidth; // 滑动一页的距离
    private int mCardGalleryWidth;

    private int mCurrentItemPos;
    private int mChoosedPostion;

    private int mCurrentItemOffset;

    private CardLinearSnapHelper mLinearSnapHelper = new CardLinearSnapHelper();

    public void attachToRecyclerView(final RecyclerView mRecyclerView) {
        // 开启log会影响滑动体验, 调试时才开启
        this.mRecyclerView = mRecyclerView;
        mContext = mRecyclerView.getContext();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLinearSnapHelper.mNoNeedToScroll = mCurrentItemOffset == 0 || mCurrentItemOffset == getDestItemOffset(mRecyclerView.getAdapter().getItemCount() - 1);
                } else {
                    mLinearSnapHelper.mNoNeedToScroll = false;
                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // dx>0则表示右滑, dx<0表示左滑, dy<0表示上滑, dy>0表示下滑
                mCurrentItemOffset += +dx;

                computeCurrentItemPos();
                onScrolledChangedCallback();
            }
        });

        initWidth();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 初始化卡片宽度
     */
    private void initWidth() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mCardGalleryWidth = mRecyclerView.getWidth();
                mCardWidth = mCardGalleryWidth - SizeUtils.dp2px(2 * (mPagePadding + mShowLeftCardWidth));
                mOnePageWidth = mCardWidth;
                mRecyclerView.smoothScrollToPosition(mCurrentItemPos);
                onScrolledChangedCallback();
            }
        });
    }

    public void setCurrentItemPos(int currentItemPos) {
        if (currentItemPos == 0 || currentItemPos == 1)
            mCurrentItemOffset = 0;
        this.mCurrentItemPos = currentItemPos;
    }

    public int getCurrentItemPos() {
        return mCurrentItemPos;
    }

    private int getDestItemOffset(int destPos) {
        return mOnePageWidth * destPos;
    }

    /**
     * 计算mCurrentItemOffset
     */
    private void computeCurrentItemPos() {
        if (mOnePageWidth <= 0) return;

        boolean pageChanged = false;
        // 滑动超过一页说明已翻页
        if (Math.abs(mCurrentItemOffset - mCurrentItemPos * mOnePageWidth) >= SizeUtils.dp2px(2 * (mPagePadding + mShowLeftCardWidth))) {
//        if (Math.abs(mCurrentItemOffset -  mOnePageWidth) >= mOnePageWidth) {

            pageChanged = true;
        }
        if (pageChanged) {
            int tempPos = mCurrentItemPos;

            mCurrentItemPos = mCurrentItemOffset / mOnePageWidth;
        }
    }

    /**
     * RecyclerView位移事件监听, view大小随位移事件变化
     */
    private void onScrolledChangedCallback() {
        int offset = mCurrentItemOffset - mCurrentItemPos * mOnePageWidth;
//        float percent = (float) Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001);
//
//        View leftView = null;
//        View currentView;
//        View rightView = null;
//        if (mCurrentItemPos > 0) {
//            leftView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos - 1);
//        }
//        currentView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos);
//        if (mCurrentItemPos < mRecyclerView.getAdapter().getItemCount() - 1) {
//            rightView = mRecyclerView.getLayoutManager().findViewByPosition(mCurrentItemPos + 1);
//        }


//        if (mChoosedPostion == mCurrentItemPos && currentView != null) {
//            currentView.setScaleY(1.1f);
//            currentView.setAlpha(1.0f);
//        } else if (currentView != null) {
//            currentView.setScaleY(1.0f);
//            currentView.setAlpha(0.8f);
//        }
//
//
//        if (mChoosedPostion - 1 == mCurrentItemPos && leftView != null) {
//            leftView.setScaleY(1.1f);
//            leftView.setAlpha(1.0f);
//        } else if (leftView != null) {
//            leftView.setScaleY(1.0f);
//            leftView.setAlpha(0.8f);
//        }
//
//
//        if (mChoosedPostion + 1 == mCurrentItemPos && rightView != null)
//
//        {
//            rightView.setScaleY(1.1f);
//            rightView.setAlpha(1.0f);
//        } else if (rightView != null) {
//            rightView.setScaleY(1.0f);
//            rightView.setAlpha(0.8f);
//        }


    }

//    public void makeChoosedBigger(int postion) {
//
//        this.mChoosedPostion = postion;
//        //  View leftView = null;
//        View currentView;
//        //   View rightView = null;
//        currentView = mRecyclerView.getLayoutManager().findViewByPosition(postion);
//
//        if (currentView != null) {
//            currentView.setScaleY(1.1f);
//            currentView.setAlpha(1.0f);
//
//        }
//
//        for (int i = postion - 1; i >=1; i--) {
//            try {
//                View view = mRecyclerView.getLayoutManager().findViewByPosition(i);
//                if (view != null) {
//                    view.setScaleY(1.0f);
//                    view.setAlpha(0.8f);
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        for (int i = postion + 1; i <=mRecyclerView.getAdapter().getItemCount(); i++) {
//            try {
//                View view = mRecyclerView.getLayoutManager().findViewByPosition(i);
//                if (view != null) {
//                    view.setScaleY(1.0f);
//                    view.setAlpha(0.8f);
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//
////        if (postion > 0) {
////            leftView = mRecyclerView.getLayoutManager().findViewByPosition(postion - 1);
////        }
////        if (postion < mRecyclerView.getAdapter().getItemCount() - 1) {
////            rightView = mRecyclerView.getLayoutManager().findViewByPosition(postion + 1);
////        }
//
//
////        if (leftView != null) {
////            leftView.setScaleY(1.0f);
////            leftView.setAlpha(0.8f);
////        }
////        if (rightView != null) {
////            rightView.setScaleY(1.0f);
////            rightView.setAlpha(0.8f);
////        }
//    }

    public void setScale(float scale) {
        mScale = scale;
    }

    public void setPagePadding(int pagePadding) {
        mPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        mShowLeftCardWidth = showLeftCardWidth;
    }
}
