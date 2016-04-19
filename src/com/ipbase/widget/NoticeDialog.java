package com.ipbase.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ipbase.R;

/**
 * @Description：通知消息内容对话框
 * @author xianhua
 * @date 2016年3月16日下午9:12:19
 */
public class NoticeDialog extends Dialog
{

	public NoticeDialog(Context context)
	{
		super(context);
	}

	public NoticeDialog(Context context, int theme)
	{
		super(context, theme);
	}

	public static class Builder
	{
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;

		public Builder(Context context)
		{
			this.context = context;
		}

		public Builder setMessage(String message)
		{
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message)
		{
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title)
		{
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title)
		{
			this.title = title;
			return this;
		}

		public Builder setContentView(View v)
		{
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener)
		{
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener)
		{
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener)
		{
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener)
		{
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public NoticeDialog create()
		{
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final NoticeDialog dialog = new NoticeDialog(context,
					R.style.NoticeDialog);
			View layout = inflater.inflate(
					R.layout.fragment_notice_content_dialog, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.tv_notice_dialog_title))
					.setText(title);
			// set the confirm button
			if (positiveButtonText != null)
			{
				((Button) layout.findViewById(R.id.btn_notice_dialog_ok))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null)
				{
					((Button) layout.findViewById(R.id.btn_notice_dialog_ok))
							.setOnClickListener(new View.OnClickListener()
							{
								public void onClick(View v)
								{
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			}
			else
			{
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.btn_notice_dialog_ok).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null)
			{
				((Button) layout.findViewById(R.id.btn_notice_dialog_cancel))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null)
				{
					((Button) layout
							.findViewById(R.id.btn_notice_dialog_cancel))
							.setOnClickListener(new View.OnClickListener()
							{
								public void onClick(View v)
								{
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			}
			else
			{
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.btn_notice_dialog_cancel)
						.setVisibility(View.GONE);
			}
			// set the content message
			if (message != null)
			{
				((TextView) layout.findViewById(R.id.tv_notice_dialog_content))
						.setText(message);
			}
			else if (contentView != null)
			{
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout
						.findViewById(R.id.tv_notice_dialog_content))
						.removeAllViews();
				((LinearLayout) layout
						.findViewById(R.id.tv_notice_dialog_content)).addView(
						contentView, new LayoutParams(LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}
	}
}
