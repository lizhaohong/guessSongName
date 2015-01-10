package com.example.moocmusicz.ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.example.moocmusicz.R;
import com.example.moocmusicz.R.id;
import com.example.moocmusicz.R.layout;
import com.example.moocmusicz.R.menu;
import com.example.moocmusicz.data.Const;
import com.example.moocmusicz.dialog.IAlertDialog;
import com.example.moocmusicz.model.IWordButtonClickListener;
import com.example.moocmusicz.model.Song;
import com.example.moocmusicz.model.WordButton;
import com.example.moocmusicz.myui.MyGridView;
import com.example.moocmusicz.util.MusicPlayer;
import com.example.moocmusicz.util.Util;
import com.example.moocmusicz.util.myLog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.R.anim;
import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


public class MainActivity extends Activity implements IWordButtonClickListener {

	public final static String TAG = "MainActivity";
	private SlidingMenu slidingMenu;
	private Button aboutAuthorButton;
	public boolean isExit = false;
	
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		     super.handleMessage(msg);  
		     isExit = false;  
		}
	};
	
	public static final int STATUS_ANSWER_RIGHT = 1;
	//答对
	public static final int STATUS_ANSWER_WRONG = 2;
	//答错
	public static final int STATUS_ANSWER_LACK = 3;
	//未答全
	public static final int TWINKLE_TIMES = 6;
	//错误的时候闪烁的次数
	public Animation mPanAnim;
	//圆盘的动画
	public LinearInterpolator mPanLin;
	
	public Animation mBarInAnim;
	//杆子的动画
	public LinearInterpolator mBarInLin;
	
	public Animation mBarOutAnim;
	//杆子出去时候的动画
	public LinearInterpolator mBarOutLin;
	
	public ImageView mViewPan;
	//圆盘
	public ImageView mViewBar;
	//杆子
	
	public View mPassView;
	//过关的界面
	
	public ImageButton mPlayButton;
	//用于播放音乐的按钮
	public boolean isRunning = false;
	//判断游戏是否进行中
	public ArrayList<WordButton> mAllWords;
	//上方待选框
	public ArrayList<WordButton> mSelectedWords;
	//下方选择框
	public MyGridView myGridView;
	
	// 已选择文字框UI容器
	public LinearLayout mWordButtonContainer;
	
	public Song mCurrentSong;
	//当前歌曲

	// 当前关的索引
	private int mCurrentStageIndex = -1;
	
	public int mCurrentCoin = Const.TOTAL_COIN_NUMBER;
	//当前金币数目
	
	private TextView mViewCurrentCoins;
	//当前修改金币数目的textview
	

    public void initSlidingMenu() {
    	slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);
        
        aboutAuthorButton = (Button) findViewById(R.id.about_author);
        aboutAuthorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO弹出作者介绍
			}
		});
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        int data[] = Util.loadData(this);
        
        mCurrentStageIndex = data[Const.DATA_FOR_STAGE_INDEX];
        mCurrentCoin = data[Const.DATA_FOR_COIN_NUMBER];
        
        initSlidingMenu();
        mViewBar = (ImageView) findViewById(R.id.bar_image_view);
        mViewPan = (ImageView) findViewById(R.id.disc_image_view);
        
        myGridView = (MyGridView) findViewById(R.id.gridview);
        
        mViewCurrentCoins = (TextView) findViewById(R.id.current_coin_number_text_view);
        mViewCurrentCoins.setText(mCurrentCoin+"");
        
        myGridView.registerListener(this);
        
        mWordButtonContainer = (LinearLayout) findViewById(R.id.word_select_container);
        
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);
        mPanAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mViewBar.startAnimation(mBarOutAnim);
			}
		});
        
        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mViewPan.startAnimation(mPanAnim);
			}
		});
        
        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				isRunning = false;
				mPlayButton.setVisibility(View.VISIBLE);
			}
		});
        
        mPlayButton = (ImageButton) findViewById(R.id.play_music_image_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handlePlayButton();
			}
		});
        
		 // 初始化游戏数据
		initCurrentStageData();
		
		// 处理删除按键事件
		handleDeleteWord();
		
		// 处理提示按键事件
		handleTipAnswer();
    }
    @Override
	public void OnWordButtonClick(WordButton wordButton) {
//		Toast.makeText(this, wordButton.mIndex + "", Toast.LENGTH_SHORT).show();
		setSelectWord(wordButton);
		
		// 获得答案状态
		int checkResult = checkTheAnswer();
		
		// 检查答案
		if (checkResult == STATUS_ANSWER_RIGHT) {
			// 过关并获得奖励
//			Toast.makeText(this, "STATUS_ANSWER_RIGHT", Toast.LENGTH_SHORT).show();
			handlePassEvent();
		} else if (checkResult == STATUS_ANSWER_WRONG) {
			// 闪烁文字并提示用户
			sparkTheWrods();
		} else if (checkResult == STATUS_ANSWER_LACK) {
			// 设置文字颜色为白色（Normal）
			for (int i = 0; i < mSelectedWords.size(); i++) {
				mSelectedWords.get(i).mButton.setTextColor(Color.WHITE);
			}
		}
	}
	
	/**
	 * 处理过关界面及事件
	 */
	private void handlePassEvent() {
		mPassView = (LinearLayout)this.findViewById(R.id.pass_view);
		
		MusicPlayer.playSong(MainActivity.this, MusicPlayer.SPECIAL_COIN_SONG);
		
		mPassView.setVisibility(View.VISIBLE);
		
		makeButtonDisClickable();
		
		MusicPlayer.stop();
		
		mViewPan.clearAnimation();

		//答对加三金币
		handleCoins(3);
		
		TextView mlevel_text_view = (TextView) findViewById(R.id.level_text_view);
		mlevel_text_view.setText((mCurrentStageIndex+1)+"");
		
		TextView msong_name_text_view = (TextView) findViewById(R.id.song_name_text_view);
		msong_name_text_view.setText(mCurrentSong.getSongName());
		
		findViewById(R.id.next_song_image_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isEndOfTheGame(mCurrentStageIndex + 1)) {
					Util.startActivity(MainActivity.this, AllPassView.class);
				} else {
					mPassView.setVisibility(View.INVISIBLE);
					makeButtonClickable();
					
					initCurrentStageData();	
				}
			}
		});
	}
	
	private void clearTheAnswer(WordButton wordButton) {
		wordButton.mButton.setText("");
		wordButton.mWordButtonName = "";
		wordButton.mVisible = false;
		
		// 设置待选框可见性
		setButtonVisiable(mAllWords.get(wordButton.mIndex), View.VISIBLE);
	}
	
	/**
	 * 设置答案
	 * 
	 * @param wordButton
	 */
	private void setSelectWord(WordButton wordButton) {
		for (int i = 0; i < mSelectedWords.size(); i++) {
			if (mSelectedWords.get(i).mWordButtonName.length() == 0) {
				// 设置答案文字框内容及可见性
				mSelectedWords.get(i).mButton.setText(wordButton.mWordButtonName);
				mSelectedWords.get(i).mVisible = true;
				mSelectedWords.get(i).mWordButtonName = wordButton.mWordButtonName;
				// 记录索引
				mSelectedWords.get(i).mIndex = wordButton.mIndex;
				
				myLog.d(TAG, mSelectedWords.get(i).mIndex + "");
				
				// 设置待选框可见性
				setButtonVisiable(wordButton, View.INVISIBLE);
				
				break;
			}
		}
	}
	
	/**
	 * 设置待选文字框是否可见
	 * 
	 * @param button
	 * @param visibility
	 */
	private void setButtonVisiable(WordButton button, int visibility) {
		button.mButton.setVisibility(visibility);
		button.mVisible = (visibility == View.VISIBLE) ? true : false;
		
		myLog.d(TAG, button.mVisible + "");
	}
	
    /**
     * 处理圆盘中间的播放按钮，就是开始播放音乐
     */
	private void handlePlayButton() {
		if (mViewBar != null) {
			if (!isRunning) {
				isRunning = true;
				
				// 开始拨杆进入动画
				mViewBar.startAnimation(mBarInAnim);
				mPlayButton.setVisibility(View.INVISIBLE);
				
				MusicPlayer.playSong(MainActivity.this,mCurrentSong.getSongFileName());
			}
		}
	}
	
	@Override
    public void onPause() {
        mViewPan.clearAnimation();
        
        Util.saveData(this, mCurrentStageIndex, mCurrentCoin);
        
        MusicPlayer.stop();
        
        super.onPause();
    }
	
	private Song loadStageSongInfo(int stageIndex) {
		Song song = new Song();
		
		String[] stage = Const.SONG_INFO[stageIndex];
		song.setSongFileName(stage[Const.INDEX_FILE_NAME]);
		song.setSongName(stage[Const.INDEX_SONG_NAME]);
		
		return song;
	}
	
	public boolean isEndOfTheGame(int currentSongIndex) {
		if(currentSongIndex < Const.SONG_INFO.length) {
			return false;
		} else if(currentSongIndex == Const.SONG_INFO.length){
			return true;
		}
		return false;
 	}
	
	public void makeButtonDisClickable() {
		((ImageButton)findViewById(R.id.delete_image_button)).setClickable(false);
		((ImageButton)findViewById(R.id.tip_image_button)).setClickable(false);
		((ImageButton)findViewById(R.id.share_image_button)).setClickable(false);
		((ImageButton)findViewById(R.id.btn_bar_add_coins)).setClickable(false);
		((ImageButton)findViewById(R.id.btn_bar_back)).setClickable(false);
	}
	
	public void makeButtonClickable() {
		((ImageButton)findViewById(R.id.delete_image_button)).setClickable(true);
		((ImageButton)findViewById(R.id.tip_image_button)).setClickable(true);
		((ImageButton)findViewById(R.id.share_image_button)).setClickable(true);
		((ImageButton)findViewById(R.id.btn_bar_add_coins)).setClickable(true);
		((ImageButton)findViewById(R.id.btn_bar_back)).setClickable(true);
	}
	
	private void initCurrentStageData() {
		// 读取当前关的歌曲信息
		
		mCurrentSong = loadStageSongInfo(++mCurrentStageIndex);
		
		MusicPlayer.playSong(MainActivity.this, mCurrentSong.getSongFileName());
		handlePlayButton();
		// 初始化已选择框
		mSelectedWords = initWordSelect();
		
		LayoutParams params = new LayoutParams(140, 140);
		
		//修改关卡的值
		((TextView)findViewById(R.id.current_level_text_view)).setText((mCurrentStageIndex+1)+"");
		
		mWordButtonContainer.removeAllViews();
		
		for (int i = 0; i < mSelectedWords.size(); i++) {
			mWordButtonContainer.addView(
					mSelectedWords.get(i).mButton,
					params);
		}
		
		// 获得数据
		mAllWords = initAllWord();
		// 更新数据- MyGridView
		myGridView.updateGridView(mAllWords);
	}
	
	/**
	 * 初始化待选文字框
	 */
	private ArrayList<WordButton> initAllWord() {
		ArrayList<WordButton> data = new ArrayList<WordButton>();
		
		// 获得所有待选文字
	    String[] words = generateWords();
		
		for (int i = 0; i < MyGridView.TOTAL_WORD_BUTTON_NUMBER; i++) {
			WordButton button = new WordButton();
			
			button.mWordButtonName = words[i];
			
			data.add(button);
		}
		
		return data;
	}
	
	/**
	 * 初始化已选择文字框
	 * 
	 * @return
	 */
	private ArrayList<WordButton> initWordSelect() {
		ArrayList<WordButton> data = new ArrayList<WordButton>();
		
		for (int i = 0; i < mCurrentSong.getSongNameLength(); i++) {
			View view = Util.getView(MainActivity.this, R.layout.self_ui_gridview_item);
			
			final WordButton holder = new WordButton();
			
			holder.mButton = (Button)view.findViewById(R.id.button_item);
			holder.mButton.setTextColor(Color.WHITE);
			holder.mButton.setText("");
			holder.mVisible = false;
			
			holder.mButton.setBackgroundResource(R.drawable.game_wordblank);
			holder.mButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					clearTheAnswer(holder);
				}
			});
			
			data.add(holder);
		}
		
		return data;
	}
	
	/**
	 * 生成所有的待选文字
	 *
	 * @return
	 */
	private String[] generateWords() {
		Random random = new Random();
		
		String[] words = new String[MyGridView.TOTAL_WORD_BUTTON_NUMBER];
		
		// 存入歌名
		for (int i = 0; i < mCurrentSong.getSongNameLength(); i++) {
			words[i] = mCurrentSong.getSongNameCharArray()[i] + "";
		}
		
		// 获取随机文字并存入数组
		for (int i = mCurrentSong.getSongNameLength(); 
				i < MyGridView.TOTAL_WORD_BUTTON_NUMBER; i++) {
			words[i] = getRandomChar() + "";
		}
		
		// 打乱文字顺序：首先从所有元素中随机选取一个与第一个元素进行交换，
		// 然后在第二个之后选择一个元素与第二个交换，知道最后一个元素。
		// 这样能够确保每个元素在每个位置的概率都是1/n。
		for (int i = MyGridView.TOTAL_WORD_BUTTON_NUMBER - 1; i >= 0; i--) {
			int index = random.nextInt(i + 1);
			
			String buf = words[index];
			words[index] = words[i];
			words[i] = buf;
		}
		
		return words;
	}
	
	/**
	 * 生成随机汉字
	 * http://www.cnblogs.com/skyivben/archive/2012/10/20/2732484.html
	 * 
	 * @return
	 */
	private char getRandomChar() {
		String str = "";
		int hightPos;
		int lowPos;
		
		Random random = new Random();
		
		hightPos = (176 + Math.abs(random.nextInt(39)));
		lowPos = (161 + Math.abs(random.nextInt(93)));
		
		byte[] b = new byte[2];
		b[0] = (Integer.valueOf(hightPos)).byteValue();
		b[1] = (Integer.valueOf(lowPos)).byteValue();
		
		try {
			str = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str.charAt(0);
	}
	
	/**
	 * 检查答案
	 * 
	 * @return
	 */
	private int checkTheAnswer() {
		// 先检查长度
		for (int i = 0; i < mSelectedWords.size(); i++) {
			// 如果有空的，说明答案还不完整
			if (mSelectedWords.get(i).mWordButtonName.length() == 0) {
				return STATUS_ANSWER_LACK;
			}
		}
		
		// 答案完整，继续检查正确性
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mSelectedWords.size(); i++) {
			sb.append(mSelectedWords.get(i).mWordButtonName);
		}
		
		return (sb.toString().equals(mCurrentSong.getSongName())) ?
				STATUS_ANSWER_RIGHT : STATUS_ANSWER_WRONG;
	}
	
	/**
	 * 文字闪烁
	 */
	private void sparkTheWrods() {
		// 定时器相关
		TimerTask task = new TimerTask() {
			boolean mChange = false;
			int mSpardTimes = 0;
			
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (++mSpardTimes > TWINKLE_TIMES) {
							return;
						}
						
						// 执行闪烁逻辑：交替显示红色和白色文字
						for (int i = 0; i < mSelectedWords.size(); i++) {
							mSelectedWords.get(i).mButton.setTextColor(
									mChange ? Color.RED : Color.WHITE);
						}
						
						mChange = !mChange;
					}
				});
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, 1, 150);
	}
	
	/**
	 * 自动选择一个答案
	 */
	private void tipAnswer() {		
		boolean tipWord = false;
		for (int i = 0; i < mSelectedWords.size(); i++) {
			if (mSelectedWords.get(i).mWordButtonName.length() == 0) {
				// 减少金币数量
				if (!handleCoins(-getTipCoins())) {
					final IAlertDialog iAlertDialog = new IAlertDialog(MainActivity.this, R.style.Transparent);
					iAlertDialog.show();
					iAlertDialog.setContent_text_view_content("很抱歉,你的金币不够");
					iAlertDialog.getCancel_image_button().setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							MusicPlayer.playSong(MainActivity.this, MusicPlayer.SPECIAL_CANCEL_SONG);
							iAlertDialog.dismiss();
						}
					});
					iAlertDialog.getOk_image_button().setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							MusicPlayer.playSong(MainActivity.this, MusicPlayer.SPECIAL_ENTER_SONG);
							iAlertDialog.dismiss();
						}
					});
					return;
				}
		
				// 根据当前的答案框条件选择对应的文字并填入
				OnWordButtonClick(findIsAnswerWord(i));
				
				tipWord = true;
				
				break;
			}
		}
		
		// 没有找到可以填充的答案
		if (!tipWord) {
			// 闪烁文字提示用户
			sparkTheWrods();
		}
	}
	
	/**
	 * 删除文字
	 */
	private void deleteOneWord() {
		// 减少金币
		if (!handleCoins(-getDeleteWordCoins())) {
			// 金币不够，显示提示对话框
			final IAlertDialog iAlertDialog = new IAlertDialog(MainActivity.this, R.style.Transparent);
			iAlertDialog.show();
			iAlertDialog.setContent_text_view_content("很抱歉,你的金币不够");
			iAlertDialog.getCancel_image_button().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MusicPlayer.playSong(MainActivity.this, MusicPlayer.SPECIAL_CANCEL_SONG);
					iAlertDialog.dismiss();
				}
			});
			iAlertDialog.getOk_image_button().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MusicPlayer.playSong(MainActivity.this, MusicPlayer.SPECIAL_ENTER_SONG);
					iAlertDialog.dismiss();
				}
			});
		return;
		}
		
		// 将这个索引对应的WordButton设置为不可见
		setButtonVisiable(findNotAnswerWord(), View.INVISIBLE);
	}
	
	/**
	 * 找到一个不是答案的文件，并且当前是可见的
	 *
	 * @return
	 */
	private WordButton findNotAnswerWord() {
		Random random = new Random();
		WordButton buf = null;
		
		while(true) {
			int index = random.nextInt(MyGridView.TOTAL_WORD_BUTTON_NUMBER);
			
			buf = mAllWords.get(index);
			
			if (buf.mVisible && !isTheAnswerWord(buf)) {
				return buf;
			}
		}
	}
	
	/**
	 * 找到一个答案文字
	 *
	 * @param index 当前需要填入答案框的索引
	 * @return
	 */
	private WordButton findIsAnswerWord(int index) {
		WordButton buf = null;
		
		for (int i = 0; i < MyGridView.TOTAL_WORD_BUTTON_NUMBER; i++) {
			buf = mAllWords.get(i);
			
			if (buf.mWordButtonName.equals("" + mCurrentSong.getSongNameCharArray()[index])) {
				return buf;
			}
		}
		
		return null;
	}
	
	/**
	 * 判断某个文字是否为答案
	 *
	 * @param word
	 * @return
	 */
	private boolean isTheAnswerWord(WordButton word) {
		boolean result = false;
		
		for (int i = 0; i < mCurrentSong.getSongNameLength(); i++) {
			if (word.mWordButtonName.equals
					("" + mCurrentSong.getSongNameCharArray()[i])) {
				result = true;
				
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * 增加或者减少指定数量的金币
	 * 
	 * @param data
	 * @return true 增加/减少成功，false 失败
	 */
	private boolean handleCoins(int data) {
		// 判断当前总的金币数量是否可被减少
		if (mCurrentCoin + data >= 0) {
			mCurrentCoin += data;
			
			mViewCurrentCoins.setText(mCurrentCoin + "");
			
			return true;
		} else {
			// 金币不够
			return false;
		}
	}
	
	/**
	 * 从配置文件里读取删除操作所要用的金币
	 *
	 * @return
	 */
	private int getDeleteWordCoins() {
		return this.getResources().getInteger(R.integer.pay_delete_word);
	}
	
    /**
     * 从配置文件里读取提示操作所要用的金币
     *
     * @return
     */
	private int getTipCoins() {
		return this.getResources().getInteger(R.integer.pay_tip_answer);
	}
	
	/**
	 * 处理删除待选文字事件
	 */
	private void handleDeleteWord() {
		ImageButton button = (ImageButton)findViewById
				(R.id.delete_image_button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final IAlertDialog iAlertDialog = new IAlertDialog(MainActivity.this, R.style.Transparent);
				iAlertDialog.show();
				iAlertDialog.setContent_text_view_content("你确定要花费30金币删除一个答案吗");
				iAlertDialog.getCancel_image_button().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MusicPlayer.playSong(MainActivity.this, MusicPlayer.SPECIAL_CANCEL_SONG);
						iAlertDialog.dismiss();
						return;
					}
				});
				iAlertDialog.getOk_image_button().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MusicPlayer.playSong(MainActivity.this, MusicPlayer.SPECIAL_ENTER_SONG);
						deleteOneWord();
						iAlertDialog.dismiss();
					}
				});
			}
		});
		
	}
	
	/**
	 * 处理提示按键事件
	 */
	private void handleTipAnswer() {
		ImageButton button = (ImageButton)findViewById
				(R.id.tip_image_button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final IAlertDialog iAlertDialog = new IAlertDialog(MainActivity.this, R.style.Transparent);
				iAlertDialog.show();
				iAlertDialog.setContent_text_view_content("你确定要花费90金币获得一个提示答案吗");
				iAlertDialog.getCancel_image_button().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MusicPlayer.playSong(MainActivity.this, MusicPlayer.SPECIAL_CANCEL_SONG);
						iAlertDialog.dismiss();
						return;
					}
				});
				iAlertDialog.getOk_image_button().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MusicPlayer.playSong(MainActivity.this, MusicPlayer.SPECIAL_ENTER_SONG);
						tipAnswer();
						iAlertDialog.dismiss();
					}
				});
			}
		});
	}
	
//	 @Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		return super.onKeyDown(keyCode, event);
//	}
}
