package cn.adminzero.helloword.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import cn.adminzero.helloword.CheckOutWordsActivity;
import cn.adminzero.helloword.R;
import cn.adminzero.helloword.ShowWordActivity;
import cn.adminzero.helloword.util.MediaPlayUtil;
import cn.adminzero.helloword.util.Words;

import cn.adminzero.helloword.util.WordsLevelUtil;
import cn.adminzero.helloword.util.WordsUtil;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class MyWordsLearningFragment extends Fragment{

    private static final String ARG_SECTION_NUMBER = "section_number";



    ArrayList<Words> wordsList;

    MediaPlayUtil player;

    public static MyWordsLearningFragment newInstance(int index) {
        MyWordsLearningFragment fragment = new MyWordsLearningFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int index = 1;
        // 提取自己是第几个Fragment
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        ArrayList<Words> testWordsList = new ArrayList<>();
        testWordsList.add(WordsUtil.getWordById((short)123));
        testWordsList.add(WordsUtil.getWordById((short)124));
        testWordsList.add(WordsUtil.getWordById((short)126));
        ArrayList<Words> test2WordList = (ArrayList<Words>) testWordsList.clone();
        ArrayList<Words> test3WordList = (ArrayList<Words>) testWordsList.clone();
        test2WordList.add(WordsUtil.getWordById((short)222));
        test3WordList.add(WordsUtil.getWordById((short)226));
        //pageViewModel.setIndex(index);
        switch (index)
        {
            // 按需要获取ArrayList
            case 0:
                // now learn
                wordsList = WordsLevelUtil.getLevel1to6();
                break;
            case 1:
                // will learn
                wordsList = WordsLevelUtil.getLevel0();

                break;
            case 2:
                // have learned
                wordsList =WordsLevelUtil.getLevel7();

                break;

        }
        player = new MediaPlayUtil();

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_check_out_words, container, false);
        // 将数据库里的单词导入到活动中显示
        ListView listView = root.findViewById(R.id.word_list_view);

        //wordsList.add(WordsUtil.getWordById((short)1));
        //wordsList.add(WordsUtil.getWordById((short)2));




        // Log.e(TAG, "onCreateView: "+wordsList.toString() );
        MyWordsLearningFragment.List_adapter list_adapter =
                new MyWordsLearningFragment.List_adapter(this.getContext(),
                        R.layout.listview_word, wordsList);
        listView.setAdapter(list_adapter);
        listView.setOnItemClickListener(wordClickedHandler);

        return root;
    }

    public void startShowWordActivity(Words words)
    {
        Intent intent = new Intent(this.getContext(), ShowWordActivity.class);
        intent.putExtra("word_to_show",words);
        intent.putExtra("from_my_words", true);
        startActivity(intent);
    }



    // 用于列表显示单词
    public class List_adapter extends ArrayAdapter<Words> {
        private int resourceId;

        public List_adapter(Context context, int textViewResourceId, List<Words> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final Words words = getItem(position);//获取当前项的Account实例

            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            // int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.WRITE_EXTERNAL_STORAGE);

            //  final View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ImageButton pronounceButton = (ImageButton) view.findViewById(R.id.pronounceButton);
            TextView word_content_textView = (TextView) view.findViewById(R.id.word_content_textView);
            TextView word_phonetic_textView = (TextView) view.findViewById(R.id.word_phonetic_textView);
            pronounceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(player!=null){
                        player.playword(words.getWord());
                    }
                    // Toast.makeText(getActivity(), words.getWord(), Toast.LENGTH_LONG).show();
                }
            });
            word_content_textView.setText(words.getWord());
            word_phonetic_textView.setText("/"+words.getPhonetic()+"/");
            //debug
            return  view;
        }


    }

    private AdapterView.OnItemClickListener wordClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id)
        {
            Words words = wordsList.get(position);
            // Toast.makeText(getActivity(), words.getWord(), Toast.LENGTH_LONG).show();
            startShowWordActivity(words);
        }
    };

}