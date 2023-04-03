package com.project.lordofthewings.Controllers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.project.lordofthewings.R;

import java.util.HashMap;
import java.util.List;

/**
 * Class that handles the expandable list view for the Comments and Authors List for QRCode
 */
public class AuthorArrayAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableTitleList;
    private HashMap<String, List<String>> expandableDetailList;

    /**
     * Constructor for the AuthorArrayAdapter
     * @param context Context of the application
     * @param expandableListTitle Parent Group Names of ListView
     * @param expandableListDetail Child Names of ListView
     */
    public AuthorArrayAdapter(Context context, List<String> expandableListTitle,
                                           HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableTitleList = expandableListTitle;
        this.expandableDetailList = expandableListDetail;
    }

    /**
     * Gets the data associated with the given child within the given group.
     * @param lstPosn Parent Group Position
     * @param expanded_ListPosition Child Position
     * @return Object of the child
     */
    @Override

    public Object getChild(int lstPosn, int expanded_ListPosition) {
        return this.expandableDetailList.get(this.expandableTitleList.get(lstPosn)).get(expanded_ListPosition);
    }

    /**
     * Gets the ID for the given child within the given group.
     * This ID must be unique across all children within the group. Hence we can pick the child uniquely
     * @param listPosition Parent Group Position
     * @param expanded_ListPosition Child Position
     * @return ID of the child
     */
    @Override
    public long getChildId(int listPosition, int expanded_ListPosition) {
        return expanded_ListPosition;
    }

    /**
     * Gets a View that displays the given child within the given group.
     * @param lstPosn Parent Group Position
     * @param expanded_ListPosition Child Position
     * @param isLastChild Boolean to check if the child is the last child
     * @param convertView View to be converted
     * @param parent Parent View
     * @return View of the child
     */
    @Override
    public View getChildView(int lstPosn, final int expanded_ListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(lstPosn, expanded_ListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.author_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.authorListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }


    /**
     * Gets the number of children in a specified group.
     * @param listPosition Parent Group Position
     * @return Number of children in the group
     */
    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableDetailList.get(this.expandableTitleList.get(listPosition)).size();
    }

    /**
     * Gets the data associated with the given group.
     * @param listPosition Parent Group Position
     * @return Object of group data
     */
    @Override
    public Object getGroup(int listPosition) {
        return this.expandableTitleList.get(listPosition);
    }

    /**
     * Gets the number of groups.
     * @return Number of groups
     */
    @Override
    public int getGroupCount() {
        return this.expandableTitleList.size();
    }

    /**
     * Gets the ID for the given group.
     * @param listPosition Parent Group Position
     * @return ID of the group
     */
    @Override
    // Gets the ID for the group at the given position. This group ID must be unique across groups.
    public long getGroupId(int listPosition) {
        return listPosition;
    }


    /**
     * Gets a View that displays the given group.
     * @param listPosition Parent Group Position
     * @param isExpanded Boolean to check if the group is expanded
     * @param convertView View to be converted
     * @param parent Parent View
     * @return View of the group parent
     */
    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.author_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.authorTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }


    /**
     * Indicates whether the child and group IDs are stable across changes to the underlying data.
     * @return Boolean value
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Indicates whether the child at the specified position is selectable.
     * @param listPosition Parent Group Position
     * @param expandedListPosition Child Position
     * @return Boolean value
     */
    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return false;
    }
}
