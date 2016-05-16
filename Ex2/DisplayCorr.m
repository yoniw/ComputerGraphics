function [ displayedCorr ] = DisplayCorr( image1,  image2, matches, dist_vals, x )
%DISPLAYCORR Summary of this function goes here
%   Detailed explanation goes here
    
    % Making sure images has the same size.
    im1_rows = size(image1, 1);
    im1_cols = size(image1, 2);
    im2_rows = size(image2, 1);
    im2_cols = size(image2, 2);
    if im1_rows >= im2_rows
        im1 = image1;
        im2 = cat(1, image2, zeroes(im1_rows - im2_rows), im2_cols);
    else
        im1 = cat(1, image1, zeroes(im2_rows - im1_rows), im1_cols);
        im2 = image2;
    end
    
    [dv_sorted, dv_indices] = sort(dist_vals);
    
    displayedCorr = [];
    x = min(x, size(matches, 1));
    % Preparing images for display with correlation indication, and building displayedCorr.
    for i = 1 : x
        match = matches(dv_indices(i), :);
        displayedCorr = [displayedCorr; match];
        
        im1_match = match(1:2);
        im2_match = match(3:4);
        % Modifying images to show match indication.
        im1 = insertText(im1, im1_match, i);
        im2 = insertText(im2, im2_match, i);
    end
    
    
    final_image = [im1 im2];
    imshow(final_image);
    
end

